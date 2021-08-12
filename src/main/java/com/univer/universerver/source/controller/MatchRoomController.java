package com.univer.universerver.source.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.dto.MatchroomDTO;
import com.univer.universerver.source.model.dto.UserDTO;
import com.univer.universerver.source.model.request.matchroom.MatchroomReq;
import com.univer.universerver.source.service.MatchingRoomService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/matchroom")
public class MatchRoomController {

	@Autowired
	private MatchingRoomService matchRoomService;
	
	@ApiOperation(value="그룹 만들기",notes="그룹 만들기")
	@PostMapping
    public ResponseEntity<MatchroomDTO> registerMatchRoom(@RequestBody MatchroomReq matchroomReq,Principal principal) {
		MatchRoom matchRoom = matchRoomService.makeGroup(matchroomReq,principal);
		return ResponseEntity.ok(new MatchroomDTO(matchRoom));
    }

}
