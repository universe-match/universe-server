package com.univer.universerver.source.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univer.universerver.source.model.request.matchroom.MatchroomReq;
import com.univer.universerver.source.service.MatchRoomService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/matchroom")
public class MatchRoomController {

	@Autowired
	private MatchRoomService matchRoomService;
	
	@ApiOperation(value="그룹 만들기",notes="그룹 만들기")
	@PostMapping
    public ResponseEntity<String> registerMatchRoom(@RequestBody MatchroomReq matchroomReq,Principal principal) {
		matchRoomService.makeGroup(matchroomReq,principal);
		return new ResponseEntity<>("성공적으로 만들어졌습니다", HttpStatus.OK);
    }

}
