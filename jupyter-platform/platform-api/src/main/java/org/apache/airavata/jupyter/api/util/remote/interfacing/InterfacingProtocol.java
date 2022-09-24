package org.apache.airavata.jupyter.api.util.remote.interfacing;

public abstract class InterfacingProtocol {

    private String remoteWorkingDir ;

    public InterfacingProtocol(String remoteWorkingDir) {
        this.remoteWorkingDir = remoteWorkingDir;
    }

    public class ExecutionResponse {
        private String stdOut;
        private String stdErr;
        private int code;

        public String getStdOut() {
            return stdOut;
        }

        public void setStdOut(String stdOut) {
            this.stdOut = stdOut;
        }

        public String getStdErr() {
            return stdErr;
        }

        public void setStdErr(String stdErr) {
            this.stdErr = stdErr;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public abstract boolean createDirectory(String relativePath) throws Exception;
    public abstract boolean transferFileToRemote(String localPath, String remoteRelativePath) throws Exception;
    public abstract boolean transferFileFromRemote(String remoteRelativePath, String localPath) throws Exception;
    public abstract ExecutionResponse executeCommand(String relativeWorkDir, String command) throws Exception;

    public String getRemoteWorkingDir() {
        return remoteWorkingDir;
    }
}
