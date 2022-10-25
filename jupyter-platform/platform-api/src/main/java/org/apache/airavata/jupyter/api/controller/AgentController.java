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

package org.apache.airavata.jupyter.api.controller;

import org.apache.airavata.jupyter.api.entity.agent.AgentHeartBeatEntity;
import org.apache.airavata.jupyter.api.entity.agent.AgentInfoEntity;
import org.apache.airavata.jupyter.api.entity.agent.AgentRegistrationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path = "/api/agent")
public class AgentController {

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    private final Map<String, AgentRegistrationEntity> agentStore = new ConcurrentHashMap<>();
    private final Map<String, AgentHeartBeatEntity> agentStatus = new ConcurrentHashMap<>();
    private final Map<String, Long> agentCheckinTimes = new ConcurrentHashMap<>();

    private long agentTimeoutMs = 30000;

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public Map<String, String> registerAgent(@RequestBody AgentRegistrationEntity agentRegistration) {
        String agentId = UUID.randomUUID().toString();
        agentStore.put(agentId, agentRegistration);
        agentCheckinTimes.put(agentId, System.currentTimeMillis());
        return Collections.singletonMap("agentId", agentId);
    }

    @PostMapping(path = "/ping", consumes = "application/json", produces = "application/json")
    public Map<String, String> receiveAgentPing(@RequestBody AgentHeartBeatEntity agentHeartBeat) {
        if (agentStore.containsKey(agentHeartBeat.getAgentId())) {
            agentCheckinTimes.put(agentHeartBeat.getAgentId(), System.currentTimeMillis());
            agentStatus.put(agentHeartBeat.getAgentId(), agentHeartBeat);
            return Collections.singletonMap("status", "Updated");
        } else {
            logger.error("Can not find a agent registration with id " + agentHeartBeat.getAgentId());
            return Collections.singletonMap("status", "Notregistered");
        }
    }

    @GetMapping(path = "/", produces = "application/json")
    public List<AgentInfoEntity> getAllAgents() {
        List<AgentInfoEntity> agentInfoEntities = new ArrayList<>();
        for (String agentId : agentStore.keySet()) {
            agentInfoEntities.add(buildAgentInfo(agentId));
        }
        return agentInfoEntities;
    }

    @GetMapping(path = "/{agentId}")
    public AgentInfoEntity getAgentInfo(@PathVariable String agentId) throws Exception {
        if (agentStore.containsKey(agentId)) {
            return buildAgentInfo(agentId);
        } else {
            logger.error("No Agent with id {}", agentId);
            throw new Exception("No Agent with id " + agentId);
        }
    }

    private AgentInfoEntity buildAgentInfo(String agentId) {
        AgentInfoEntity agentInfo = new AgentInfoEntity();
        agentInfo.setAgentId(agentId);
        AgentRegistrationEntity agentRegistration = agentStore.get(agentId);
        agentInfo.setAgentApiUrl(agentRegistration.getAgentApiUrl());
        agentInfo.setCpus(agentRegistration.getCpus());
        agentInfo.setMemory(agentRegistration.getMemory());
        agentInfo.setGpuMemory(agentRegistration.getGpuMemory());

        if (agentCheckinTimes.containsKey(agentId)) {
            agentInfo.setLastCheckedInTime(agentCheckinTimes.get(agentId));
        }

        if (agentStatus.containsKey(agentId)) {
            AgentHeartBeatEntity agentHeartBeat = agentStatus.get(agentId);
            agentInfo.setCpuLoad(agentHeartBeat.getCpuLoad());
            agentInfo.setMemoryRemaining(agentHeartBeat.getMemoryRemaining());
            agentInfo.setGpuMemoryRemaining(agentHeartBeat.getGpuMemoryRemaining());
        }
        return agentInfo;
    }
}
