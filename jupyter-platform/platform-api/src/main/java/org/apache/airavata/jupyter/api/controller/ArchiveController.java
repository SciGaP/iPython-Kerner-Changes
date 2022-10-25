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
import org.apache.airavata.jupyter.api.entity.job.JobEntity;
import org.apache.airavata.jupyter.api.repo.ArchiveRepository;
import org.apache.airavata.jupyter.api.repo.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping(path = "/api/archive")
public class ArchiveController {
    private static final Logger logger = LoggerFactory.getLogger(ArchiveController.class);

    @org.springframework.beans.factory.annotation.Value("${archive.upload.dir}")
    private String uploadPath = "/tmp/";

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private JobRepository jobRepository;

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ArchiveEntity createArchive(@RequestBody ArchiveEntity archiveEntity) {
        ArchiveEntity saved = archiveRepository.save(archiveEntity);
        return saved;
    }

    @GetMapping(path = "/")
    public List<ArchiveEntity> listArchives() {
        Iterable<ArchiveEntity> all = archiveRepository.findAll();
        List<ArchiveEntity> archives = new ArrayList<>();
        all.forEach(archive -> {
            if (! (archive.getDescription().startsWith("HPC Export") || archive.getDescription().startsWith("Archive for UI Container"))) {
                archives.add(archive);
            }
        });
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

    @GetMapping (value = "/download_archive/{archiveId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> downloadArchive(@PathVariable String archiveId,
                                                                 final HttpServletResponse response) throws Exception {
        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=ARCHIVE.zip");

        ArchiveEntity archive = getArchive(archiveId);
        StreamingResponseBody stream = out -> {

            ServletOutputStream os = response.getOutputStream();
            try(FileInputStream is = new FileInputStream(archive.getPath())) {
                byte[] bytes=new byte[1024];
                int length;
                while ((length=is.read(bytes)) >= 0) {
                    os.write(bytes, 0, length);
                }

                os.close();
            }
        };

        return new ResponseEntity(stream, HttpStatus.OK);
    }

    @GetMapping (value = "/download/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> download(@PathVariable String jobId, final HttpServletResponse response)
            throws Exception {
        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=REMOTE_STATE.zip");

        StreamingResponseBody stream = out2 -> {


            Optional<JobEntity> jobOp = jobRepository.findById(jobId);
            JobEntity job = jobOp.get();
            final File directory = new File(job.getLocalWorkingPath());
            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

            if(directory.exists() && directory.isDirectory()) {
                try {
                    for (final File file : directory.listFiles()) {
                        final InputStream inputStream=new FileInputStream(file);
                        final ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);
                        byte[] bytes=new byte[1024];
                        int length;
                        while ((length=inputStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        inputStream.close();
                    }
                    zipOut.close();
                } catch (final IOException e) {
                    logger.error("Exception while reading and streaming data {} ", e);
                }
            }
        };
        logger.info("steaming response {} ", stream);
        return new ResponseEntity(stream, HttpStatus.OK);
    }
}
