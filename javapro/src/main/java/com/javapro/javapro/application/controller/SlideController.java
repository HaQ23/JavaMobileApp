package com.javapro.javapro.application.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SlideController {

    @Value("${slides.base-path}")
    private String slidesBasePath;

    private final ResourceLoader resourceLoader;

    public SlideController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/api/slides/{lecture}")
    public List<String> getSlides(@PathVariable String lecture) {
        List<String> slideNames = new ArrayList<>();
        Resource lectureFolder = resourceLoader.getResource(slidesBasePath + "/" + lecture);

        try {
            if (lectureFolder.exists() && lectureFolder.getFile().isDirectory()) {
                for (File file : lectureFolder.getFile().listFiles()) {
                    slideNames.add(file.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return slideNames;
    }
}
