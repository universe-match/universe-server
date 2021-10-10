package com.univer.universerver.source.controller;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.Message;
import com.univer.universerver.source.model.response.ChatRoomResponse;
import com.univer.universerver.source.model.response.MatchRoomResponse;
import com.univer.universerver.source.service.ChatRoomService;
import com.univer.universerver.source.service.MessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    private MessageService messageService;

    @ApiOperation(value="채팅리스트 조회",notes="채팅리스트 조회")
    @GetMapping
    public ResponseEntity<?> getMatchRoomList(Pageable pageable, Principal principal) {

        List<ChatRoomResponse> chatRoomList= chatRoomService.selectChatRoomList();
        return ResponseEntity.ok(chatRoomList);

    }
    @ApiOperation(value="채팅방내용 조회",notes="채팅방내용 조회")
    @GetMapping("/{chatroomId}")
    public ResponseEntity<?> getMatchRoomInfo(Principal principal, @PathVariable(name = "chatroomId") long id) {

        List<Message> chatRoomInfo = messageService.selectChatroomInfo(id);
        return ResponseEntity.ok(chatRoomInfo);
    }
}
