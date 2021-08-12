package com.univer.universerver.source.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
