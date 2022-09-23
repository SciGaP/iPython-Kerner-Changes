package org.apache.airavata.jupyter.api.util.remote.submitters;

public class SlurmJobSubmitter implements JobSubmitter {
    @Override
    public String submitJob(String archivePath, String sessionId) throws Exception {
        return null;
    }

    @Override
    public String getJobStatus(String jobId) throws Exception {
        return null;
    }

    @Override
    public String cancelJob(String jobId) throws Exception {
        return null;
    }
}
