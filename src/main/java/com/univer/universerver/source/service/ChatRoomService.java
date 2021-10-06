package com.univer.universerver.source.service;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.response.ChatRoomResponse;
import com.univer.universerver.source.model.response.MatchRoomResponse;
import com.univer.universerver.source.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public void insertChatRoomPeople(Long matchRoomId) {

        MatchRoom matchRoom = new MatchRoom();
        matchRoom.setId(matchRoomId);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setMatchRoom(matchRoom);

        chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomResponse> selectChatRoomList() {
        List<ChatRoom> chatRoom = chatRoomRepository.findAll();
//        List<MatchRoomResponse> matchRoomRes = matchRoom
//                .stream()
//                .map(item->new MatchRoomResponse(item))
//                .collect(Collectors.toList());
        List<ChatRoomResponse> chatRoomList = chatRoom.stream().map(item->new ChatRoomResponse(item)).collect(Collectors.toList());
        return chatRoomList;
    }
}
