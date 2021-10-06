package com.univer.universerver.source.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String,String>> upload(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        
		Map<String,String> map=new HashMap<String, String>();
		String imgUrl = s3Uploader.upload(multipartFile, "user");
		map.put("imgUrl",imgUrl);
       
		return ResponseEntity.ok(map);
    }
	@PostMapping("/user/certi/image")
	public ResponseEntity<Map<String,String>> uploadCerti(@RequestParam("image") MultipartFile multipartFile) throws IOException {

		Map<String,String> map=new HashMap<String, String>();
		String imgUrl = s3Uploader.upload(multipartFile, "certification");
		map.put("imgUrl",imgUrl);

		return ResponseEntity.ok(map);
	}
}
