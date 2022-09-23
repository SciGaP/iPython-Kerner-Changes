package org.apache.airavata.jupyter.api.util.remote.interfacing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalInterfacingProtocol extends InterfacingProtocol {

    private static final Logger logger = LoggerFactory.getLogger(LocalInterfacingProtocol.class);

    public LocalInterfacingProtocol(String remoteWorkingDir) {
        super(remoteWorkingDir);
    }

    @Override
    public boolean createDirectory(String relativePath) throws Exception {
        Files.createDirectories(Path.of(getRemoteWorkingDir(), relativePath));
        return true;
    }

    @Override
    public boolean transferFileToRemote(String localPath, String remoteRelativePath) throws Exception {
        Files.copy(Path.of(localPath), Path.of(getRemoteWorkingDir(), remoteRelativePath));
        return true;
    }

    @Override
    public boolean transferFileFromRemote(String remoteRelativePath, String localPath) throws Exception {
        Files.copy(Path.of(getRemoteWorkingDir(), remoteRelativePath), Path.of(localPath));
        return true;
    }

    @Override
    public ExecutionResponse executeCommand(String relativeWorkDir, String command) throws Exception {

        Runtime rt = Runtime.getRuntime();
        String[] envs= {};
        Process proc = rt.exec(command, envs,  new File(getRemoteWorkingDir() + "/" + relativeWorkDir));

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));


        logger.info("Here is the standard output of the command:\n");
        String s = null;
        StringBuilder stdOut = new StringBuilder();
        StringBuilder stdErr = new StringBuilder();
        while ((s = stdInput.readLine()) != null) {
            logger.info(s);
            stdOut.append(s).append("\n");
        }

        logger.info("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            logger.info(s);
            stdErr.append(s).append("\n");
        }

        ExecutionResponse response = new ExecutionResponse();
        response.setStdOut(stdOut.toString());
        response.setStdErr(stdErr.toString());
        response.setCode(proc.exitValue());

        return response;
    }
}
