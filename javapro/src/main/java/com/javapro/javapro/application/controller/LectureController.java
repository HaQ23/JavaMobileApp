package com.javapro.javapro.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LectureController {

    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

    @Value("${slides.base-path}")
    private String slidesBasePath;

    private final ResourceLoader resourceLoader;

    public LectureController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/api/lectures")
    public List<String> getLectures() {
        List<String> lectureNames = new ArrayList<>();
        Resource slidesFolderResource = resourceLoader.getResource(slidesBasePath);

        try {
            File slidesFolder = slidesFolderResource.getFile();
            logger.info("Looking for lectures in folder: {}", slidesFolder.getAbsolutePath());

            if (slidesFolder.exists() && slidesFolder.isDirectory()) {
                for (File file : slidesFolder.listFiles()) {
                    logger.info("Found file/folder: {}", file.getName());
                    if (file.isDirectory()) {
                        logger.info("Adding lecture folder: {}", file.getName());
                        lectureNames.add(file.getName());
                    }
                }
            } else {
                logger.warn("Folder {} does not exist or is not a directory", slidesFolder.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Error accessing the slides folder", e);
        }

        return lectureNames;
    }
}
