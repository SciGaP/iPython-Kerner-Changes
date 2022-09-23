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

import org.apache.airavata.jupyter.api.entity.ArchiveEntity;
import org.apache.airavata.jupyter.api.entity.interfacing.LocalInterfaceEntity;
import org.apache.airavata.jupyter.api.entity.interfacing.SSHInterfaceEntity;
import org.apache.airavata.jupyter.api.entity.remote.ComputeEntity;
import org.apache.airavata.jupyter.api.repo.*;
import org.apache.airavata.jupyter.api.util.remote.interfacing.InterfacingProtocol;
import org.apache.airavata.jupyter.api.util.remote.interfacing.LocalInterfacingProtocol;
import org.apache.airavata.jupyter.api.util.remote.interfacing.SSHInterfacingProtocol;
import org.apache.airavata.jupyter.api.util.remote.submitters.ForkJobSubmitter;
import org.apache.airavata.jupyter.api.util.remote.submitters.JobSubmitter;
import org.apache.airavata.jupyter.api.util.remote.submitters.SlurmJobSubmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/remote")
public class RemoteExecController {
    private static final Logger logger = LoggerFactory.getLogger(RemoteExecController.class);

    private String localWorkingDir = "/tmp";

    @Autowired
    private ComputeRepository computeRepository;

    @Autowired
    private LocalInterfaceRepository localInterfaceRepository;

    @Autowired
    private SSHInterfaceRepository sshInterfaceRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobStatusRepository jobStatusRepository;

    public class RunCellResponse {
        private String jobId;

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }
    }

    @GetMapping(path = "/run/{computeId}/{archiveId}/{sessionId}")
    public RunCellResponse runCell(@PathVariable String computeId, @PathVariable String archiveId, @PathVariable String sessionId) throws Exception {

        logger.info("Running cell for compute {} with state archive uploaded in to archive {}", computeId, archiveId);

        Optional<ArchiveEntity> archiveOp = archiveRepository.findById(archiveId);
        Optional<ComputeEntity> computeOp = computeRepository.findById(computeId);
        if (computeOp.isPresent() && archiveOp.isPresent()) {
            ComputeEntity computeEntity = computeOp.get();
            ArchiveEntity archiveEntity = archiveOp.get();
            InterfacingProtocol interfacingProtocol = resolveInterface(computeEntity.getInterfaceType(), computeEntity.getInterfaceId());

            // Creating local working directory
            String workDirForCurrent = localWorkingDir + "/"  + UUID.randomUUID().toString();
            Files.createDirectory(Path.of(workDirForCurrent));

            JobSubmitter jobSubmitter = resolveJobSubmitter(interfacingProtocol, computeEntity.getSubmitterType(), workDirForCurrent);
            String jobId = jobSubmitter.submitJob(archiveEntity.getPath(), sessionId);
            RunCellResponse response = new RunCellResponse();
            response.setJobId(jobId);
            return response;
        } else {
            throw new Exception("Could not find a compute resource with id " + computeId + " or archive with id " + archiveId);
        }
    }

    @PostMapping(path = "/interface/local", consumes = "application/json", produces = "application/json")
    public LocalInterfaceEntity createLocalInterface(Authentication authentication, @RequestBody LocalInterfaceEntity localInterfaceEntity) {
        LocalInterfaceEntity saved = localInterfaceRepository.save(localInterfaceEntity);
        return saved;
    }

    @PostMapping(path = "/interface/ssh", consumes = "application/json", produces = "application/json")
    public SSHInterfaceEntity createSSHInterface(Authentication authentication, @RequestBody SSHInterfaceEntity sshInterfaceEntity) {
        SSHInterfaceEntity saved = sshInterfaceRepository.save(sshInterfaceEntity);
        return saved;
    }

    @PostMapping(path = "/compute", consumes = "application/json", produces = "application/json")
    public ComputeEntity createCompute(Authentication authentication, @RequestBody ComputeEntity computeEntity) {
        ComputeEntity saved = computeRepository.save(computeEntity);
        return saved;
    }

    private JobSubmitter resolveJobSubmitter(InterfacingProtocol interfacingProtocol,
                                             ComputeEntity.SubmitterType submitterType,
                                             String workDir) throws Exception {
        switch (submitterType) {
            case FORK:
                return new ForkJobSubmitter(workDir, interfacingProtocol, jobRepository, jobStatusRepository);
            case SLURM:
                return new SlurmJobSubmitter();
        }

        throw new Exception("Could not find a job submitter with type " + submitterType.name());

    }

    private InterfacingProtocol resolveInterface(ComputeEntity.InterfaceType interfaceType, String interfaceId) throws Exception {

        switch (interfaceType) {
            case LOCAL:
                Optional<LocalInterfaceEntity> localInterfaceOp = localInterfaceRepository.findById(interfaceId);
                if (localInterfaceOp.isPresent()) {
                    return new LocalInterfacingProtocol(localInterfaceOp.get().getWorkingDirectory());
                } else {
                    throw new Exception("Could not find a local interface with id " + interfaceId);
                }
            case SSH:
                Optional<SSHInterfaceEntity> sshInterfaceOp = sshInterfaceRepository.findById(interfaceId);
                if (sshInterfaceOp.isPresent()) {
                     return new SSHInterfacingProtocol(sshInterfaceOp.get(), sshInterfaceOp.get().getWorkingDirectory());
                } else {
                    throw new Exception("Could not find a SSH interface with id " + interfaceId);
                }
        }

        throw new Exception("Could not find a valid interface for type " + interfaceType.name() + " and id " + interfaceId);
    }
}
