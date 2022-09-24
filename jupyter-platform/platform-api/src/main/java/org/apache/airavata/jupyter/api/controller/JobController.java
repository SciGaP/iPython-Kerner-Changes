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

import org.apache.airavata.jupyter.api.entity.job.JobStatusEntity;
import org.apache.airavata.jupyter.api.repo.JobRepository;
import org.apache.airavata.jupyter.api.repo.JobStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/job")
public class JobController {

    @Autowired
    private JobStatusRepository jobStatusRepository;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping(path = "/status/{jobId}")
    public JobStatusEntity getJobStatus(@PathVariable String jobId) throws Exception {
        Optional<JobStatusEntity> jobSt = jobStatusRepository.findFirstByJobIdOrderByUpdatedTimeAsc(jobId);
        return jobSt.orElseThrow(() -> new Exception("Could not find job status for job id " + jobId));
    }
}
