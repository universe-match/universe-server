package com.univer.universerver.source.service;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.response.ChatRoomResponse;
import com.univer.universerver.source.model.response.MatchRoomResponse;
import com.univer.universerver.source.model.response.UserResponse;
import com.univer.universerver.source.pushnoti.service.NotificationService;
import com.univer.universerver.source.repository.ChatRoomRepository;
import com.univer.universerver.source.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;


    public ChatRoom insertChatRoomPeople(Long matchRoomId) {

        MatchRoom matchRoom = new MatchRoom();
        matchRoom.setId(matchRoomId);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setMatchRoom(matchRoom);

        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomResponse> selectChatRoomList(String userId) {
        Optional<User> user= userRepository.findByUserid(userId);
        List<ChatRoom> chatRoom = chatRoomRepository.findAllByMatchRoomMatchingListUser(user.get());
//        List<MatchRoomResponse> matchRoomRes = matchRoom
//                .stream()
//                .map(item->new MatchRoomResponse(item))
//                .collect(Collectors.toList());
        List<ChatRoomResponse> chatRoomList = chatRoom.stream().map(item->new ChatRoomResponse(item)).collect(Collectors.toList());
        return chatRoomList;
    }

    public void sendMsg(Long chatroomId,long userId) {
        Optional<ChatRoom> chatroom = chatRoomRepository.findById(chatroomId);
        Optional<User> user = userRepository.findById(userId);

        chatroom.get().getChatRoomUser().stream().forEach(item->{
            if(item.getUser().getFcmToken()!= null){
                notificationService.sendNoti(item.getUser().getNickname(),item.getUser().getFcmToken(),"채팅방에 메세지가 왔습니다",user.get().getNickname()+"님이 메세지를 보냈습니다.");
            }
        });
    }

    public List<UserResponse> selectchatRoomPeopleInfo(long chatroomId) {
        Optional<ChatRoom> chatroom = chatRoomRepository.findById(chatroomId);
        List<UserResponse> chatRoomList = chatroom.get().getMatchRoom()
                                                        .getMatchingList()
                                                        .stream()
                                                        .map(item->new UserResponse(item.getUser()))
                                                        .collect(Collectors.toList());
        return chatRoomList;
    }

    public ChatRoom findChatRoom(long chatroomId) {
        return chatRoomRepository.findById(chatroomId).get();
    }
}
