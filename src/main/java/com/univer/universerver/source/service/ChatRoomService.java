package com.univer.universerver.source.service;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.response.ChatRoomResponse;
import com.univer.universerver.source.model.response.MatchRoomResponse;
import com.univer.universerver.source.repository.ChatRoomRepository;
import com.univer.universerver.source.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;

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
}
