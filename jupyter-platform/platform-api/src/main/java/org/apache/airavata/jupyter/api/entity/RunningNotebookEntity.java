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

package org.apache.airavata.jupyter.api.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "RUNNING_NOTEBOOK")
public class RunningNotebookEntity {

    @Id
    @Column(name = "RUNNING_NOTEBOOK_ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "NOTEBOOK_ID")
    private String notebookId;

    @Column(name = "CONTAINER_ID")
    private String containerId;

    @Column(name = "LAUNCH_TIME")
    private long launchTime;

    @Column(name = "BIND_PORT")
    private int bindPort;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "OWNER")
    private String owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(String notebookId) {
        this.notebookId = notebookId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public int getBindPort() {
        return bindPort;
    }

    public void setBindPort(int bindPort) {
        this.bindPort = bindPort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public static final class RunningNotebookEntityBuilder {
        private String notebookId;
        private String containerId;
        private long launchTime;
        private int bindPort;
        private boolean active;
        private String token;
        private String owner;

        private RunningNotebookEntityBuilder() {
        }

        public static RunningNotebookEntityBuilder builder() {
            return new RunningNotebookEntityBuilder();
        }


        public RunningNotebookEntityBuilder withNotebookId(String notebookId) {
            this.notebookId = notebookId;
            return this;
        }

        public RunningNotebookEntityBuilder withContainerId(String containerId) {
            this.containerId = containerId;
            return this;
        }

        public RunningNotebookEntityBuilder withLaunchTime(long launchTime) {
            this.launchTime = launchTime;
            return this;
        }

        public RunningNotebookEntityBuilder withBindPort(int bindPort) {
            this.bindPort = bindPort;
            return this;
        }

        public RunningNotebookEntityBuilder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public RunningNotebookEntityBuilder withToken(String token) {
            this.token = token;
            return this;
        }

        public RunningNotebookEntityBuilder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public RunningNotebookEntity build() {
            RunningNotebookEntity runningNotebookEntity = new RunningNotebookEntity();
            runningNotebookEntity.setNotebookId(notebookId);
            runningNotebookEntity.setContainerId(containerId);
            runningNotebookEntity.setLaunchTime(launchTime);
            runningNotebookEntity.setBindPort(bindPort);
            runningNotebookEntity.setActive(active);
            runningNotebookEntity.setToken(token);
            runningNotebookEntity.setOwner(owner);
            return runningNotebookEntity;
        }
    }
}
