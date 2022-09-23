package org.apache.airavata.jupyter.api.entity.job;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "JOB")
public class JobEntity {

    @Id
    @Column(name = "JOB_ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "REMOTE_WORK_PATH")
    private String remoteWorkingPath;

    @Column(name = "LOCAL_WORK_PATH")
    private String localWorkingPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemoteWorkingPath() {
        return remoteWorkingPath;
    }

    public void setRemoteWorkingPath(String remoteWorkingPath) {
        this.remoteWorkingPath = remoteWorkingPath;
    }

    public String getLocalWorkingPath() {
        return localWorkingPath;
    }

    public void setLocalWorkingPath(String localWorkingPath) {
        this.localWorkingPath = localWorkingPath;
    }
}
