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
import org.apache.airavata.jupyter.api.entity.ArchiveEntity;
import org.apache.airavata.jupyter.api.entity.NotebookEntity;
import org.apache.airavata.jupyter.api.entity.RunningNotebookEntity;
import org.apache.airavata.jupyter.api.repo.ArchiveRepository;
import org.apache.airavata.jupyter.api.repo.NotebookRepository;
import org.apache.airavata.jupyter.api.repo.RunningNotebookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class OrchestrationEngine {

    private static final Logger logger = LoggerFactory.getLogger(OrchestrationEngine.class);

    private String baseImage = "dimuthuupe/ipykernel:1.2";
    private String containerInputPath = "/opt/ARCHIVE.zip";
    private int containerPort = 8888;
    private String portRange = "33000:35000";
    private String tempDir = "/tmp";

    private final ExecutorService launchQueue = Executors.newFixedThreadPool(20);

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private RunningNotebookRepository runningNotebookRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

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
                Pair<String, Integer> runResp = runContainer(notebook, environment);
                RunningNotebookEntity rne = RunningNotebookEntity.RunningNotebookEntityBuilder.builder()
                        .withNotebookId(notebook.getId())
                        .withLaunchTime(System.currentTimeMillis())
                        .withBindPort(runResp.getSecond())
                        .withContainerId(runResp.getFirst())
                        .withToken(notebookToken)
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

    private Pair<String, Integer> runContainer(NotebookEntity notebook, Map<String, String> environmentValues)
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

        logger.info("Created the container with id " + containerResponse.getId());

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


    private int getAvailablePort(String portRange) throws Exception {
        String[] parts = portRange.split(":");
        for (int port = Integer.parseInt(parts[0]); port <= Integer.parseInt(parts[1]); port++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", port), 1000);
                socket.close();
            } catch (Exception ex) {
                return port;
            }
        }
        throw new Exception("No open port available in range " + portRange);
    }
}
