package com.univer.universerver.source.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.univer.universerver.source.model.Matching;
import com.univer.universerver.source.model.dto.MatchingDTO;
import com.univer.universerver.source.model.request.matching.MatchingReq;
import com.univer.universerver.source.service.MatchingService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/match")
public class MatchingController {

	@Autowired
	private MatchingService matchingService;
	
	@ApiOperation(value="그룹 참여",notes="그룹 참여")
	@PostMapping
    public ResponseEntity<?> insertMatching(@RequestBody MatchingReq matchingReq,Principal principal) {
		
		Matching matching = matchingService.insertPeople(matchingReq,principal);
		
		return ResponseEntity.ok(new MatchingDTO(matching.getId(), matching.getUser().getId()));
    }
	@ApiOperation(value="그룹 나가기",notes="그룹 나가기")
	@DeleteMapping("/{chatroomId}")
	public ResponseEntity<?> deleteMatching(@PathVariable(name = "chatroomId") long chatroomId,Principal principal) {

		matchingService.deleteMatching(chatroomId,principal);

		return ResponseEntity.ok("성공적으로 삭제되었습니다");
	}
}
