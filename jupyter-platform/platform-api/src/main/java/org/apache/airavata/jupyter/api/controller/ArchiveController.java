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
import org.apache.airavata.jupyter.api.entity.NotebookEntity;
import org.apache.airavata.jupyter.api.repo.ArchiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping(path = "/archive")
public class ArchiveController {
    private static final Logger logger = LoggerFactory.getLogger(ArchiveController.class);

    private String uploadPath = "/tmp/";

    @Autowired
    private ArchiveRepository archiveRepository;

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ArchiveEntity createArchive(@RequestBody ArchiveEntity archiveEntity) {
        ArchiveEntity saved = archiveRepository.save(archiveEntity);
        return saved;
    }

    @GetMapping(path = "/")
    public List<ArchiveEntity> listArchives() {
        Iterable<ArchiveEntity> all = archiveRepository.findAll();
        List<ArchiveEntity> archives = new ArrayList<>();
        all.forEach(archives::add);
        return archives;
    }

    @GetMapping(path = "/{archiveId}")
    public ArchiveEntity getArchive(@PathVariable String archiveId) throws Exception{
        Optional<ArchiveEntity> byId = archiveRepository.findById(archiveId);
        return byId.orElseThrow(() -> new Exception("No archive found with id " + archiveId));
    }

    @PostMapping("/upload")
    public Map<String, String> singleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {

        byte[] bytes = file.getBytes();
        Path path = Paths.get(uploadPath + UUID.randomUUID().toString());
        Files.write(path, bytes);

        return Collections.singletonMap("path", path.toAbsolutePath().toString());
    }
}
