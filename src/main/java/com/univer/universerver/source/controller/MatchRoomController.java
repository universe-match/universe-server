package com.univer.universerver.source.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.univer.universerver.source.model.response.MatchRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	@ApiOperation(value="그룹리스트 조회",notes="그룹리스트 조회")
	@GetMapping
	public ResponseEntity<?> getMatchRoomList(Pageable pageable, Principal principal) {
		Page<MatchRoom> matchRoom = matchRoomService.getMatchRoomList(pageable,principal);
		List<MatchRoomResponse> matchRoomRes = matchRoom
												.stream()
												.map(item->new MatchRoomResponse(item))
												.collect(Collectors.toList());
		Page<MatchRoomResponse> rtnMatchRoom = new PageImpl<>(matchRoomRes);
		return ResponseEntity.ok(rtnMatchRoom);

	}
}
