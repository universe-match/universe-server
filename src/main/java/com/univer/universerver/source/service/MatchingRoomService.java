package com.univer.universerver.source.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.univer.universerver.source.model.response.MatchRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.univer.universerver.source.common.response.ErrorCode;
import com.univer.universerver.source.common.response.exception.MatchRoomDuplicateException;
import com.univer.universerver.source.common.response.exception.UserException;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.request.matchroom.MatchroomReq;
import com.univer.universerver.source.repository.MatchRoomRepository;
import com.univer.universerver.source.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MatchingRoomService {

	@Autowired
	private MatchRoomRepository matchRoomRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MatchingService matchingService;
	@Autowired
	private ChatRoomService chatRoomService;
	
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
		
		MatchRoom rtnMatchRoom = matchRoomRepository.save(matchroom);
		matchingService.insertInvitedPeople(rtnMatchRoom.getId(), user.get().getId());
		chatRoomService.insertChatRoomPeople(rtnMatchRoom.getId());
		if(matchroomReq.getFriends().length>0) {
			for(long friend:matchroomReq.getFriends()) {
				matchingService.insertInvitedPeople(rtnMatchRoom.getId(), friend);
			}	
		}
		
		return rtnMatchRoom;
	}

    public Page<MatchRoom> getMatchRoomList(Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
		pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
		return matchRoomRepository.findAll(pageable);
    }
}
