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

public class AgentHeartBeatEntity {
    private String agentId;
    private long memoryRemaining;
    private long gpuMemoryRemaining;
    private double cpuLoad;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getMemoryRemaining() {
        return memoryRemaining;
    }

    public void setMemoryRemaining(long memoryRemaining) {
        this.memoryRemaining = memoryRemaining;
    }

    public long getGpuMemoryRemaining() {
        return gpuMemoryRemaining;
    }

    public void setGpuMemoryRemaining(long gpuMemoryRemaining) {
        this.gpuMemoryRemaining = gpuMemoryRemaining;
    }

    public double getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }
}
