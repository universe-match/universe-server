package com.univer.universerver.source.service;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.univer.universerver.source.common.response.ErrorCode;
import com.univer.universerver.source.common.response.exception.MatchRoomDuplicateException;
import com.univer.universerver.source.common.response.exception.UserException;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.request.matchroom.MatchroomReq;
import com.univer.universerver.source.repository.MatchRoomRepository;
import com.univer.universerver.source.repository.UserRepository;

@Service
public class MatchingRoomService {

	@Autowired
	private MatchRoomRepository matchRoomRepository;
	@Autowired
	private UserRepository userRepository;
	
	public MatchRoom makeGroup(MatchroomReq matchroomReq, Principal principal) {
		
		if(principal==null) {
			throw new UserException(ErrorCode.NOT_LOGIN);
		}
		Optional<User> user= userRepository.findByUserid(principal.getName());
		boolean existMakeUser = matchRoomRepository.existsByUserId(user.get().getId());
		if(existMakeUser) {
			throw new MatchRoomDuplicateException(ErrorCode.MATCHROOM_DUPLICATION);
		}
		MatchRoom matchroom =new MatchRoom();
		matchroom.setTitle(matchroomReq.getTitle());
		matchroom.setContent(matchroomReq.getContent());
		matchroom.setPlace(matchroomReq.getPlace());
		matchroom.setPeopleLimit(matchroomReq.getPeopleLimit());
		matchroom.setGenderkind(matchroomReq.getGenderKind());
		matchroom.setGroupkind(matchroomReq.getGroupKind());
		matchroom.setFromDate(matchroomReq.getFromDate());
		matchroom.setToDate(matchroomReq.getToDate());
		matchroom.setUser(user.get());
		return matchRoomRepository.save(matchroom);
	}

}
