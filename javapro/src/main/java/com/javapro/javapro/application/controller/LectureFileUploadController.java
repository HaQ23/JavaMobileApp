package com.javapro.javapro.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/lecture")
public class LectureFileUploadController {

    private final String UPLOAD_DIR_BASE = "src/main/resources/static/uploads/";

    @PostMapping("/uploadSlide")
    public ResponseEntity<?> uploadSlide(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) {
        try {
            String uploadDir = UPLOAD_DIR_BASE + folder;
            Path uploadPath = Paths.get(uploadDir);

            // Tworzenie folderu, jeśli nie istnieje
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path copyLocation = Paths.get(uploadPath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("Slide uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the slide");
        }
    }

    @PostMapping("/createLecture")
    public ResponseEntity<?> createLecture(@RequestParam("folder") String folder) {
        try {
            String uploadDir = UPLOAD_DIR_BASE + folder;
            Path uploadPath = Paths.get(uploadDir);

            // Tworzenie folderu, jeśli nie istnieje
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            return ResponseEntity.ok("Lecture folder created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create lecture folder");
        }
    }
}
