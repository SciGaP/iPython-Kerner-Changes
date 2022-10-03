package org.apache.airavata.jupyter.api.entity.ui;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "UI_APP")
public class UIAppEntity {

    @Id
    @Column(name = "UI_APP_ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "DOCKER_IMAGE")
    private String dockerImageName;

    @Column(name = "INTERNAL_VNC_PORT")
    private int internalVncPort;

    @Column(name = "INPUT_DIR")
    private String inputDir;

    @Column(name = "VNC_PASSWORD")
    private String vncPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDockerImageName() {
        return dockerImageName;
    }

    public void setDockerImageName(String dockerImageName) {
        this.dockerImageName = dockerImageName;
    }

    public int getInternalVncPort() {
        return internalVncPort;
    }

    public void setInternalVncPort(int internalVncPort) {
        this.internalVncPort = internalVncPort;
    }

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVncPassword() {
        return vncPassword;
    }

    public void setVncPassword(String vncPassword) {
        this.vncPassword = vncPassword;
    }
}
