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

import org.apache.airavata.jupyter.api.entity.NotebookEntity;
import org.apache.airavata.jupyter.api.entity.RunningNotebookEntity;
import org.apache.airavata.jupyter.api.repo.NotebookRepository;
import org.apache.airavata.jupyter.api.repo.RunningNotebookRepository;
import org.apache.airavata.jupyter.core.OrchestrationEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/nb")
public class NotebookController extends DefaultErrorAttributes {

    private static final Logger logger = LoggerFactory.getLogger(NotebookController.class);

    @Autowired
    private NotebookRepository nbRepo;

    @Autowired
    private RunningNotebookRepository rnbRepo;

    @Autowired
    private OrchestrationEngine orchestrationEngine;

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public NotebookEntity createNotebook(Authentication authentication, @RequestBody NotebookEntity notebookEntity) {
        notebookEntity.setOwner(authentication.getName());
        NotebookEntity saved = nbRepo.save(notebookEntity);
        return saved;
    }

    @GetMapping(path = "/")
    public List<NotebookEntity> listCreatedNotebooks(Authentication authentication) {
        Iterable<NotebookEntity> allNotebooks = nbRepo.findAll();
        List<NotebookEntity> nbs = new ArrayList<>();
        allNotebooks.forEach(nbs::add);
        return nbs;
    }

    @GetMapping(path = "/launch/{noteBookId}")
    public String launchNotebook(Authentication authentication, @PathVariable String noteBookId, @QueryParam(value = "force") boolean force) throws Exception {
        Optional<NotebookEntity> notebookOp = nbRepo.findById(noteBookId);
        try {
            orchestrationEngine.launchNotebook(notebookOp.get(), force);
        } catch (Exception e) {
            logger.error("Failed to launch notebook with id {}", noteBookId);
            throw e;
        }
        return "SUCCESS";
    }

    @GetMapping(path = "/kill/{noteBookId}")
    public String killNotebook(Authentication authentication, @PathVariable String noteBookId) {
        Optional<NotebookEntity> notebookOp = nbRepo.findById(noteBookId);
        orchestrationEngine.killNotebook(notebookOp.get());
        return "SUCCESS";
    }

    @GetMapping(path = "/launched/{noteBookId}")
    public List<RunningNotebookEntity> getRunningNotebookSessions(Authentication authentication, @PathVariable String noteBookId) {
        return rnbRepo.findRunningNotebookEntityByNotebookIdAndActive(noteBookId, true);
    }

    @GetMapping(path = "/launched")
    public List<RunningNotebookEntity> getAllRunningNotebookSessions(Authentication authentication) {
        return rnbRepo.findRunningNotebookEntityByActive(true);
    }
}
