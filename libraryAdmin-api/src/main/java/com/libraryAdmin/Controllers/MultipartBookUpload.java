package com.libraryAdmin.Controllers;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class MultipartBookUpload {


    private Logger logger = (Logger) LoggerFactory.getLogger(MultipartBookUpload.class);

    @PostMapping("/multiple/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        // Check if files are empty
        if (files == null || files.length == 0) {
            return new ResponseEntity<>("No files uploaded", HttpStatus.BAD_REQUEST);
        }

        this.logger.info("{} number of files uploaded", files.length);

        // Process each file
        for (MultipartFile file : files) {
            // Perform file processing logic here (e.g., saving to disk, processing, etc.)
            // For demonstration purposes, print file details
            System.out.println("Uploaded file name: " + file.getOriginalFilename());
            System.out.println("Uploaded file size: " + file.getSize());
        }

        return new ResponseEntity<>("Files uploaded successfully", HttpStatus.OK);
    }
}
