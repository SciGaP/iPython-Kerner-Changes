package org.apache.airavata.jupyter.api.util.remote.submitters;

import org.apache.airavata.jupyter.api.entity.job.JobEntity;
import org.apache.airavata.jupyter.api.entity.job.JobStatusEntity;
import org.apache.airavata.jupyter.api.repo.JobRepository;
import org.apache.airavata.jupyter.api.repo.JobStatusRepository;
import org.apache.airavata.jupyter.api.util.remote.interfacing.InterfacingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.UUID;

public class ForkJobSubmitter implements JobSubmitter {

    private static final Logger logger = LoggerFactory.getLogger(ForkJobSubmitter.class);

    private InterfacingProtocol interfacingProtocol;

    private String codeTemplate = "import dill as pickle\n" +
            "import shutil\n" +
            "import json\n" +
            "import subprocess\n"+
            "import sys\n" +
            "import os\n" +
            "\n" +
            "f = open('ARCHIVE/files.json')\n" +
            "files_json = json.load(f)\n" +
            "for v in files_json:\n" +
            "    target_path = files_json[v]\n" +
            "    if not target_path.startswith(\"/\"):\n" +
            "        target_path = target_path\n" +
            "\n" +
            "    dir_path = os.path.dirname(target_path)\n" +
            "    if not dir_path == \"\":\n" +
            "        os.makedirs(dir_path, exist_ok = True)\n" +
            "\n" +
            "    shutil.copyfile('ARCHIVE/' + v, target_path)\n" +
            "f = open('ARCHIVE/dependencies.json')\n" +
            "dep_json = json.load(f)\n" +
            "subprocess.check_call([sys.executable, \"-m\", \"pip\", \"install\", \"--upgrade\", \"pip\"])\n" +
            "for dep_name in dep_json:\n" +
            "    dep_version = dep_json[dep_name]\n" +
            "    subprocess.check_call([sys.executable, \"-m\", \"pip\", \"install\", dep_name + \"==\" + dep_version])\n" +
            "with open('ARCHIVE/context.p', 'rb') as f:\n" +
            "    context = pickle.load(f)\n" +
            "\n" +
            "with open('ARCHIVE/code.txt') as f:" +
            "\n" +
            "   code = f.read()" +
            "\n" +
            "exec(code, None, context)" +
            "\n" +
            "with open('ARCHIVE/final-context.p', 'wb') as f:\n" +
            "    pickle.dump(context, f)\n";

    private String localWorkingDir;
    private JobRepository jobRepository;
    private JobStatusRepository jobStatusRepository;

    public ForkJobSubmitter(String localWorkingDir,
                            InterfacingProtocol interfacingProtocol,
                            JobRepository jobRepository,
                            JobStatusRepository jobStatusRepository) {
        this.localWorkingDir = localWorkingDir;
        this.interfacingProtocol = interfacingProtocol;
        this.jobRepository = jobRepository;
        this.jobStatusRepository = jobStatusRepository;
    }

    @Override
    public String submitJob(String archivePath, String sessionId) throws Exception {
        String expDir = UUID.randomUUID().toString();
        String sessionDir = "sessions/" + sessionId;
        logger.info("Using experiment directory {} and working directory {}",
                interfacingProtocol.getRemoteWorkingDir() + "/" + expDir, localWorkingDir);
        interfacingProtocol.createDirectory(expDir);

        // This to store a virtual environment which can be reused across the same session
        interfacingProtocol.createDirectory(sessionDir);
        logger.info("Created exp dir in {} and session dir in {}", expDir, sessionDir);

        InterfacingProtocol.ExecutionResponse response = interfacingProtocol.executeCommand(sessionDir, "python3 -m venv venv --system-site-packages");
        if (response.getCode() != 0) {
            logger.error("Failed to create the virtual environment. Stderr: " + response.getStdErr() + " Std out " + response.getStdOut());
            throw new Exception("Failed to create the virtual environment");
        }

        String pythonCommand = interfacingProtocol.getRemoteWorkingDir() + "/" + sessionDir + "/venv/bin/python3";
        String pipCommand = interfacingProtocol.getRemoteWorkingDir() + "/" + sessionDir + "/venv/bin/pip3";
        // TODO Save in database

        interfacingProtocol.transferFileToRemote(archivePath, expDir + "/ARCHIVE.zip");
        interfacingProtocol.executeCommand(expDir, "unzip ARCHIVE.zip -d ARCHIVE");

        BufferedWriter writer = new BufferedWriter(new FileWriter(localWorkingDir + "/wrapper_code.py"));
        writer.write(codeTemplate);
        writer.flush();
        writer.close();

        interfacingProtocol.transferFileToRemote(localWorkingDir + "/wrapper_code.py", expDir + "/wrapper_code.py");

        interfacingProtocol.executeCommand(expDir, pipCommand + " install dill");
        InterfacingProtocol.ExecutionResponse executionResponse = interfacingProtocol.executeCommand(expDir, pythonCommand + " wrapper_code.py");

        writer = new BufferedWriter(new FileWriter(localWorkingDir + "/stdout.txt"));
        writer.write(executionResponse.getStdOut());
        writer.flush();
        writer.close();

        writer = new BufferedWriter(new FileWriter(localWorkingDir + "/stderr.txt"));
        writer.write(executionResponse.getStdErr());
        writer.flush();
        writer.close();

        writer = new BufferedWriter(new FileWriter(localWorkingDir + "/state-code.txt"));
        writer.write(executionResponse.getCode() + "");
        writer.flush();
        writer.close();

        interfacingProtocol.transferFileFromRemote(expDir + "/ARCHIVE/final-context.p", localWorkingDir + "/final-context.p");

        logger.info("Completed running cell and placed output in {}", localWorkingDir);

        JobEntity jobEntity = new JobEntity();
        jobEntity.setLocalWorkingPath(localWorkingDir);
        jobEntity.setRemoteWorkingPath(expDir);
        JobEntity savedJob = jobRepository.save(jobEntity);

        JobStatusEntity jobStatusEntity = new JobStatusEntity();
        jobStatusEntity.setJobEntity(jobEntity);
        jobStatusEntity.setState(JobStatusEntity.State.COMPLETED);
        jobStatusEntity.setUpdatedTime(System.currentTimeMillis());
        jobStatusRepository.save(jobStatusEntity);

        return savedJob.getId();
    }

    @Override
    public String getJobStatus(String jobId) {
        return null;
    }

    @Override
    public String cancelJob(String jobId) {
        return null;
    }
}
