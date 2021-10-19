package com.univer.universerver.source.service;

import java.security.Principal;
import java.util.Optional;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.univer.universerver.source.common.response.ErrorCode;
import com.univer.universerver.source.common.response.exception.UserException;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.Matching;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.dto.MatchingDTO;
import com.univer.universerver.source.model.request.matching.MatchingReq;
import com.univer.universerver.source.repository.MatchRoomRepository;
import com.univer.universerver.source.repository.MatchingRepository;
import com.univer.universerver.source.repository.UserRepository;

@Service
public class MatchingService {

	@Autowired
	private MatchingRepository matchingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MatchRoomRepository matchRoomRepository;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private ChatRoomUserService chatRoomUserService;
	public Matching insertPeople(MatchingReq matchingReq, Principal principal) {
		
		if(principal==null) {
			throw new UserException(ErrorCode.NOT_LOGIN);
		}else {
			Optional<User> user= userRepository.findByUserid(principal.getName());
			
			Optional<MatchRoom> matchRoomD = matchRoomRepository.findById(matchingReq.getMid());

			if(matchRoomD.get().getUser().getId()==user.get().getId()) {
				throw new UserException(ErrorCode.MATCHING_ROOMMASTER_DUPLICATION);
			}
			
			long matchingCnt =matchingRepository.countByMatchRoom(matchRoomD.get());
			
			if(matchingCnt+1>matchRoomD.get().getPeopleLimit()) {
				throw new UserException(ErrorCode.MATCHROOM_LIMIT_EXCEED);
			}
			boolean inPeople = matchingRepository.existsByUser(user.get());
			
			if(inPeople) {
				throw new UserException(ErrorCode.MATCHROOM_INPEOPLE_DUPLICATION);
			}else {
				
				MatchRoom matchRoom = new MatchRoom();
				matchRoom.setId(matchingReq.getMid());
				
				Matching matching = new Matching();
				matching.setMatchRoom(matchRoom);
				matching.setUser(user.get());
				matching.setAgree('N');
				ChatRoom chatRoom = chatRoomRepository.findByMatchRoom(matchRoom);
				chatRoomUserService.insertChatRoomUser(chatRoom,user.get());
				return matchingRepository.save(matching);		
			}
		}
	}
	public Matching insertInvitedPeople(long mId, long friend) {
		
		Optional<User> user= userRepository.findById(friend);
		
		Optional<MatchRoom> matchRoomD = matchRoomRepository.findById(mId);

//		if(matchRoomD.get().getUser().getId()==user.get().getId()) {
//			throw new UserException(ErrorCode.MATCHING_ROOMMASTER_DUPLICATION);
//		}
		
		long matchingCnt =matchingRepository.countByMatchRoom(matchRoomD.get());
		
		if(matchingCnt+1>=matchRoomD.get().getPeopleLimit()) {
			throw new UserException(ErrorCode.MATCHROOM_LIMIT_EXCEED);
		}

		
//		boolean inPeople = matchingRepository.existsByUser(user.get());
		
//		if(inPeople) {
//			throw new UserException(ErrorCode.MATCHROOM_INPEOPLE_DUPLICATION);
//		}else {
//
//			MatchRoom matchRoom = new MatchRoom();
//			matchRoom.setId(mId);
//
//			Matching matching = new Matching();
//			matching.setMatchRoom(matchRoom);
//			matching.setUser(user.get());
//			matching.setAgree('N');
//			return matchingRepository.save(matching);
//		}
		MatchRoom matchRoom = new MatchRoom();
		matchRoom.setId(mId);

		Matching matching = new Matching();
		matching.setMatchRoom(matchRoom);
		matching.setUser(user.get());
		matching.setAgree('N');
		return matchingRepository.save(matching);

	}

}
