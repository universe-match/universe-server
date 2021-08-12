package com.univer.universerver.source.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univer.universerver.source.model.ChatMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("!stomp")
@Component
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
//    private final ChatRoomRepository repository;

    @Autowired
    public ChatHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
//        this.repository = chatRoomRepository;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        log.info("payload : {}", payload);
       
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        System.out.println(chatMessage);
        //ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        ChatRoom chatRoom = repository.getChatRoom(chatMessage.getChatRoomId());
//        chatRoom.handleMessage(session, chatMessage, objectMapper);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        repository.remove(session);
    }
}