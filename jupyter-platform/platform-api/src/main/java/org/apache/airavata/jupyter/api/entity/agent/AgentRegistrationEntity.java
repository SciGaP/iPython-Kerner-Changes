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

package org.apache.airavata.jupyter.api.entity.agent;

public class AgentRegistrationEntity {
    private String agentApiUrl;
    private String agentName;
    private int cpus;
    private long memory;
    private long gpuMemory;

    public String getAgentApiUrl() {
        return agentApiUrl;
    }

    public void setAgentApiUrl(String agentApiUrl) {
        this.agentApiUrl = agentApiUrl;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public long getGpuMemory() {
        return gpuMemory;
    }

    public void setGpuMemory(long gpuMemory) {
        this.gpuMemory = gpuMemory;
    }
}
