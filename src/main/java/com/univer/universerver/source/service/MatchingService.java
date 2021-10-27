package com.univer.universerver.source.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;

@Service
@Transactional
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
			boolean inPeople = matchingRepository.existsByUserAndMatchRoomId(user.get(),matchingReq.getMid());
//
			if(inPeople) {
				throw new UserException(ErrorCode.MATCHROOM_INPEOPLE_DUPLICATION);
			}else {

				MatchRoom matchRoom = new MatchRoom();
				matchRoom.setId(matchingReq.getMid());

				Matching matching = new Matching();
				matching.setMatchRoom(matchRoom);
				matching.setUser(user.get());
				matching.setAgree('N');
				matching.setMasterYn('N');
				ChatRoom chatRoom = chatRoomRepository.findByMatchRoom(matchRoom);
//				ChatRoom newChatRoom = new ChatRoom();
//				newChatRoom.setMatchRoom(chatRoom.getMatchRoom());
//				newChatRoom.setChatRoomUsers(newChatRoom.getChatRoomUsers());
				chatRoomUserService.insertChatRoomUser(chatRoom,user.get());
				return matchingRepository.save(matching);
			}
//			MatchRoom matchRoom = new MatchRoom();
//			matchRoom.setId(matchingReq.getMid());
//
//			Matching matching = new Matching();
//			matching.setMatchRoom(matchRoom);
//			matching.setUser(user.get());
//			matching.setAgree('N');
//			ChatRoom chatRoom = chatRoomRepository.findByMatchRoom(matchRoom);
//			chatRoomUserService.insertChatRoomUser(chatRoom,user.get());
//			return matchingRepository.save(matching);
		}
	}
	public Matching insertInvitedPeople(long mId, long friend,char masterYn) {
		
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
		matching.setMasterYn(masterYn);
		return matchingRepository.save(matching);

	}

	public void deleteMatching(long chatroomId,Principal principal) {
		Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatroomId);
		long matchRoomId = chatRoom.get().getMatchRoom().getId();

		Optional<User> user= userRepository.findByUserid(principal.getName());

		chatRoom.get().getMatchRoom().getMatchingList().stream().forEach(item-> {
			if(item.getUser().getId()==user.get().getId()){

				if(item.getMasterYn()=='Y'){
					Long minUserId = matchingRepository.selectMinUserId(matchRoomId);


					matchingRepository.deleteByMatchRoomIdAndUserId(matchRoomId,user.get().getId());
					chatRoomUserService.deleteUser(user.get(),chatRoom.get().getId());
					if(minUserId!=null){
						Optional<Matching> minUser = matchingRepository.findById(minUserId);
						Optional<MatchRoom> matchRoom = matchRoomRepository.findByUserId(user.get().getId());
						minUser.get().setMasterYn('Y');
						matchingRepository.save(minUser.get());
//						matchRoom.ifPresent(mRoom->{
//							User newMaster =new User();
//							newMaster.setId(minUser.get().getUser().getId());
//							mRoom.setUser(newMaster);
//							matchRoomRepository.save(mRoom);
//						});
					}else{
						chatRoomRepository.deleteById(chatRoom.get().getId());
						matchRoomRepository.deleteById(matchRoomId);

					}
				}else{
					matchingRepository.deleteByMatchRoomIdAndUserId(matchRoomId,user.get().getId());
					chatRoomUserService.deleteUser(user.get(),chatRoom.get().getId());
				}
			}


		});


//		chatRoomRepository.deleteByIdAndMatchRoomId();
		//chatRoomRepository.deleteById(chatroomId);
	}


	public List<Matching> findMatchRoom(Long matchroomid) {
		return matchingRepository.findByMatchRoomId(matchroomid);
	}

	public void deleteUser(String userId, Long matchRoomId) {
		matchingRepository.deleteByMatchRoomIdAndUserId(matchRoomId,Long.parseLong(userId));
	}
}
