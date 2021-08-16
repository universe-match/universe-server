package com.univer.universerver.source.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.univer.universerver.source.config.S3Uploader;

@RestController
public class UploadController {
    
    @Autowired
    private S3Uploader s3Uploader;
    
	@PostMapping("/user/image")
    public ResponseEntity<String> upload(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        String imgUrl = s3Uploader.upload(multipartFile, "user");
		return ResponseEntity.ok(imgUrl);

    }
}
