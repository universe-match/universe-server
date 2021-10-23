package com.univer.universerver.source.controller;

import com.univer.universerver.source.service.ComplainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Map;

@Controller
public class ComplainController {

    @Autowired
    private ComplainService complainService;

    @ApiOperation(value="문의작성",notes="문의작성")
    @PostMapping("/api/complain")
    public ResponseEntity<?> myInfoDelete(@RequestBody Map<String, String> param) {
        String content = param.get("content");
        complainService.saveContent(content);
        return ResponseEntity.ok("문의가 접수되었습니다.");
    }
}
