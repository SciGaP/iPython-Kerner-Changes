package org.apache.airavata.jupyter.api.util.remote.submitters;

public interface JobSubmitter {

    public String submitJob(String archivePath, String sessionId) throws Exception;
    public String getJobStatus(String jobId) throws  Exception;
    public String cancelJob(String jobId) throws  Exception;
}
