/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.airavata.jupyter.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.apache.airavata.jupyter.api.controller.AgentController;
import org.apache.airavata.jupyter.api.entity.ArchiveEntity;
import org.apache.airavata.jupyter.api.entity.NotebookEntity;
import org.apache.airavata.jupyter.api.entity.RunningNotebookEntity;
import org.apache.airavata.jupyter.api.entity.agent.AgentInfoEntity;
import org.apache.airavata.jupyter.api.entity.ui.UIAppEntity;
import org.apache.airavata.jupyter.api.entity.ui.UIExecutionEntity;
import org.apache.airavata.jupyter.api.entity.ui.UIExecutionResponseEntity;
import org.apache.airavata.jupyter.api.repo.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OrchestrationEngine {

    private static final Logger logger = LoggerFactory.getLogger(OrchestrationEngine.class);

    @org.springframework.beans.factory.annotation.Value("${notebook.container.image}")
    private String baseImage;

    private String containerInputPath = "/opt/ARCHIVE.zip";
    private int containerPort = 8888;

    @org.springframework.beans.factory.annotation.Value("${port.bind.range}")
    private String portRange;

    @org.springframework.beans.factory.annotation.Value("${vnc.bind.host}")
    private String vncBindHost;

    @org.springframework.beans.factory.annotation.Value("${vnc.bin.home}")
    private String vncHomeDir;

    @org.springframework.beans.factory.annotation.Value("${vnc.bin.name}")
    private String vncBin;

    @org.springframework.beans.factory.annotation.Value("${docker.host.ip}")
    private String dockerHostIp;

    private final ExecutorService launchQueue = Executors.newFixedThreadPool(20);

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private RunningNotebookRepository runningNotebookRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private UIAppRepository uiAppRepository;

    @Autowired
    private UIExecutionRepository uiExecutionRepository;

    @Autowired
    private AgentController agentController;

    public void launchNotebook(NotebookEntity notebook, boolean force) throws Exception {

        launchQueue.submit(() -> {
            try {
                logger.info("Launching notebook {}", notebook.getId());

                List<RunningNotebookEntity> runningNotebooks = runningNotebookRepository
                        .findRunningNotebookEntityByNotebookIdAndActive(notebook.getId(), true);

                if (!force && runningNotebooks.size() > 0) {
                    logger.error("There are {} number of notebooks running under notebook id {}",
                            runningNotebooks.size(), notebook.getId());
                    throw new Exception("There are notebooks running for id " + notebook.getId());
                } else {
                    killNotebook(notebook);
                }

                String notebookToken = UUID.randomUUID().toString();
                HashMap<String, String> environment = new HashMap<>();
                environment.put("NOTEBOOK_TOKEN", notebookToken);
                environment.put("DOCKER_HOST_IP", dockerHostIp);
                Pair<String, Integer> runResp = runNotebookContainer(notebook, environment);
                RunningNotebookEntity rne = RunningNotebookEntity.RunningNotebookEntityBuilder.builder()
                        .withNotebookId(notebook.getId())
                        .withLaunchTime(System.currentTimeMillis())
                        .withBindPort(runResp.getSecond())
                        .withContainerId(runResp.getFirst())
                        .withToken(notebookToken)
                        .withOwner(notebook.getOwner())
                        .withActive(true).build();
                runningNotebookRepository.save(rne);

                logger.info("Completed launching notebook {} on container {} with port {}",
                        notebook.getId(), runResp.getFirst(), runResp.getSecond());
                return runResp.getFirst();
            } catch (Exception e) {
                logger.error("Failed while launching the notebook {}", notebook.getId(), e);
                throw e;
            }
        });
    }

    public UIExecutionResponseEntity launchUI(UIExecutionEntity uiExecutionEntity) throws Exception {

        UIExecutionEntity savedExecutionEty = uiExecutionRepository.save(uiExecutionEntity);

        Optional<UIAppEntity> appEtyOp = uiAppRepository.findFirstByName(savedExecutionEty.getAppName());

        UIAppEntity uiAppEntity = appEtyOp.orElseThrow(() -> new Exception("No UI App " + savedExecutionEty.getAppName()));

        AgentInfoEntity agentInfo = agentController.getAgentInfo(uiExecutionEntity.getTargetAgentId());

        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(agentInfo.getAgentApiUrl() + "/ui/launch");

        String json = "{\n" +
                "    \"executionId\": \"" + savedExecutionEty.getId()+ "\",\n" +
                "    \"dockerImageName\": \"" + uiAppEntity.getName() + "\",\n" +
                "    \"internalVncPort\": " + uiAppEntity.getInternalVncPort() + ",\n" +
                "    \"vncPassword\": \"" + uiAppEntity.getVncPassword() + "\"\n" +
                "}";

        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            HttpEntity httpResponseEntity = response.getEntity();
            Header encodingHeader = httpResponseEntity.getContentEncoding();

            String jsonOut = EntityUtils.toString(httpResponseEntity, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(jsonOut);

            UIExecutionResponseEntity responseEntity = new UIExecutionResponseEntity();
            responseEntity.setId(UUID.randomUUID().toString());
            responseEntity.setExecutionState(UIExecutionResponseEntity.ExecutionState.OK);
            responseEntity.setMessage("Successfully started container " + jsonObject.getString("containerId"));
            responseEntity.setContainerId(jsonObject.getString("containerId"));
            responseEntity.setVncUrl(jsonObject.getString("vncUrl"));
            responseEntity.setExecutionId(savedExecutionEty.getId());

            return responseEntity;
        } else {
            throw new Exception("Failed to submit launch UI request. Error code " + statusCode);
        }
    }

    public void killNotebook(NotebookEntity notebook) {
        List<RunningNotebookEntity> runningNotebooks = runningNotebookRepository
                .findRunningNotebookEntityByNotebookIdAndActive(notebook.getId(), true);
        if (runningNotebooks.size() > 0) {

            logger.info("Killing already running notebook sessions for {}", notebook.getId());
            for (RunningNotebookEntity nbe: runningNotebooks) {
                stopContainer(nbe.getContainerId());
                nbe.setActive(false);
                runningNotebookRepository.save(nbe);
            }
        }
    }

    private final void stopContainer(String containerId) {
        DefaultDockerClientConfig.Builder config = DefaultDockerClientConfig.createDefaultConfigBuilder();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config.build()).build();

        List<Container> resp = dockerClient.listContainersCmd().withIdFilter(Collections.singletonList(containerId)).exec();
        if (resp.size() > 0) {
            Container container = resp.get(0);
            logger.info("Container {} state is {}", containerId, container.getState());
            if ("running".equals(container.getState())) {
                logger.info("Stopping container {}", containerId);
                dockerClient.stopContainerCmd(containerId).exec();
            }
            logger.info("Removing the container {}", containerId);
            dockerClient.removeContainerCmd(containerId).exec();
        }

    }

    private Pair<String, Integer> runNotebookContainer(NotebookEntity notebook, Map<String, String> environmentValues)
            throws Exception{

        DefaultDockerClientConfig.Builder config = DefaultDockerClientConfig.createDefaultConfigBuilder();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config.build()).build();

        logger.info("Pulling image " + baseImage);
        try {
            dockerClient.pullImageCmd(baseImage.split(":")[0])
                    .withTag(baseImage.split(":")[1])
                    .exec(new PullImageResultCallback()).awaitCompletion();
        } catch (InterruptedException e) {
            logger.error("Interrupted while pulling image", e);
            throw e;
        }

        logger.info("Successfully pulled image " + baseImage);

        String containerId = UUID.randomUUID().toString();
        int mappingPort = getAvailablePort(portRange);

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withPortBindings(PortBinding.parse(mappingPort + ":" + containerPort))
                .withCapAdd(Capability.SYS_PTRACE);

        if (notebook.getArchiveId() != null) {
            Optional<ArchiveEntity> archiveOp = archiveRepository.findById(notebook.getArchiveId());
            if (archiveOp.isPresent()) {
                hostConfig.withBinds(Bind.parse(archiveOp.get().getPath() + ":" + containerInputPath));
            } else {
                logger.warn("Archive id {} is specified in notebook {} but not found", notebook.getArchiveId(),
                        notebook.getId());
            }
        }

        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(baseImage).withName(containerId)
                .withTty(true)
                .withAttachStdin(true)
                .withHostConfig(hostConfig)
                .withAttachStdout(true).withEnv(environmentValues.entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.toList()))
                .exec();

        logger.info("Created the Notebook container with id " + containerResponse.getId());

        if (containerResponse.getWarnings() != null && containerResponse.getWarnings().length > 0) {
            StringBuilder warningStr = new StringBuilder();
            for (String w : containerResponse.getWarnings()) {
                warningStr.append(w).append(",");
            }
            logger.warn("Container " + containerResponse.getId() + " warnings : " + warningStr);
            throw new Exception("Failed to start the container");
        } else {
            logger.info("Starting container with id {} and mapped port {} for notebook {}",
                    containerResponse.getId(), mappingPort, notebook.getId());
            dockerClient.startContainerCmd(containerResponse.getId()).exec();
            return Pair.of(containerResponse.getId(), mappingPort);
        }
    }

    private Pair<String, Integer> runUIContainer(UIExecutionEntity executionEntity,
                                                 UIAppEntity uiAppEntity) throws Exception {

        DefaultDockerClientConfig.Builder config = DefaultDockerClientConfig.createDefaultConfigBuilder();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config.build()).build();


         // TODO pull image

        String containerId = UUID.randomUUID().toString();
        int mappingPort = getAvailablePort(portRange);

        logger.info("Using mapping port {} for UI App {}", mappingPort, uiAppEntity.getId());

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withPortBindings(PortBinding.parse(mappingPort + ":" + uiAppEntity.getInternalVncPort()))
                .withCapAdd(Capability.SYS_PTRACE);

        if (executionEntity.getArchiveId() != null) {
            Optional<ArchiveEntity> archiveOp = archiveRepository.findById(executionEntity.getArchiveId());
            if (archiveOp.isPresent()) {
                hostConfig.withBinds(Bind.parse(archiveOp.get().getPath() + ":" + containerInputPath));
            } else {
                logger.warn("Archive id {} is specified in UI execution request {} but not found", executionEntity.getArchiveId(),
                        executionEntity.getAppName());
            }
        }

        CreateContainerResponse containerResponse = dockerClient
                .createContainerCmd(uiAppEntity.getDockerImageName()).withName(containerId)
                .withTty(true)
                .withAttachStdin(true)
                .withEnv("DOCKER_HOST_IP="+dockerHostIp, "EXECUTION_ID=" + executionEntity.getId())
                .withHostConfig(hostConfig)
                .withAttachStdout(true)
                .exec();

        logger.info("Created the UI container with id " + containerResponse.getId());


        if (containerResponse.getWarnings() != null && containerResponse.getWarnings().length > 0) {
            StringBuilder warningStr = new StringBuilder();
            for (String w : containerResponse.getWarnings()) {
                warningStr.append(w).append(",");
            }
            logger.warn("UI Container " + containerResponse.getId() + " warnings : " + warningStr);
            throw new Exception("Failed to start the UI container");
        } else {
            logger.info("Starting UI container with id {} and mapped port {} for UI App {}",
                    containerResponse.getId(), mappingPort, executionEntity.getAppName());
            dockerClient.startContainerCmd(containerResponse.getId()).exec();
            return Pair.of(containerResponse.getId(), mappingPort);
        }
    }

    public void killNoVncSession(String agentId, String containerId) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        AgentInfoEntity agentInfo = agentController.getAgentInfo(agentId);
        HttpGet httpGet = new HttpGet(agentInfo.getAgentApiUrl() + "/ui/killNoVnc/" + containerId);

        httpGet.setHeader("Accept", "application/json");

        CloseableHttpResponse response = client.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != 200) {
            throw new Exception("Failed to kill novnc session for container " + containerId + " . Error code " + statusCode);
        }
    }

    private class VncProcessReader implements Runnable {

        private InputStream inputStream;
        private Consumer<String> consumer;

        public VncProcessReader(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    public String checkUIContainerStatus(String agentId, String containerId) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        AgentInfoEntity agentInfo = agentController.getAgentInfo(agentId);
        HttpGet httpGet = new HttpGet(agentInfo.getAgentApiUrl() + "/ui/container/status/" + containerId);

        httpGet.setHeader("Accept", "application/json");

        CloseableHttpResponse response = client.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            HttpEntity httpResponseEntity = response.getEntity();

            String jsonOut = EntityUtils.toString(httpResponseEntity, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(jsonOut);

            return jsonObject.getString("status");
        } else {
            throw new Exception("Failed to fetch container status from Agent. Error code " + statusCode);
        }

    }

    private int getAvailablePort(String portRange, int ... excludes) throws Exception {
        String[] parts = portRange.split(":");
        for (int port = Integer.parseInt(parts[0]); port <= Integer.parseInt(parts[1]); port++) {
            if (! isPortOpen(port)) {
                final int selectedPort = port;
                if (! Arrays.stream(excludes).filter(exlude -> exlude == selectedPort).findAny().isPresent()) {
                    return port;
                }
            }
        }
        throw new Exception("No open port available in range " + portRange);
    }

    private boolean isPortOpen(int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", port), 1000);
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
