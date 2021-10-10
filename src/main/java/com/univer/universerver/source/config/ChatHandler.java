package com.univer.universerver.source.config;

import com.univer.universerver.source.common.response.ErrorCode;
import com.univer.universerver.source.common.response.exception.CommonException;
import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.Message;
import com.univer.universerver.source.model.MessageType;
import com.univer.universerver.source.repository.ChatRoomRepository;
import com.univer.universerver.source.service.MessageService;
import com.univer.universerver.source.utils.JsonUtil;
import com.univer.universerver.source.utils.LocalDateTimeUtil;
import org.json.simple.JSONObject;
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

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Profile("!stomp")
@Component
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
//    private final ChatRoomRepository repository;

    private static final String TYPE = "type";
    private static final String SESSION_ID = "sessionId";
    private static final String CHATROOM_ID = "chatroomId";
    private static final long USER_KEY = 0;
    private static final String USERNAME = "username";
    private static final String MESSAGE = "message";

    @Autowired
    public ChatHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
//        this.repository = chatRoomRepository;
    }
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private MessageService messageService;

    public static final Map<Long, Map<String, WebSocketSession>> chatroomMap = new HashMap<>();

    @PostConstruct
    private void init() {

        List<ChatRoom> chatrooms = chatRoomRepository.findAll();
        chatrooms.stream().forEach(chatroom -> {
            chatroomMap.put(chatroom.getId(), new HashMap<>());
        });
    }
    // 소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String uri = session.getUri().toString();

        // TODO - or getQuery : ?chatroomId=1
        Long chatroomId = Long.valueOf(uri.split("/chat/")[1]);
        if (chatroomMap.containsKey(chatroomId)) {

            Map<String, WebSocketSession> sessionMap = chatroomMap.get(chatroomId);
            sessionMap.put(session.getId(), session);

            log.info("------------ Connection Establised ------------");
            super.afterConnectionEstablished(session);

            JSONObject object = new JSONObject();
            object.put(TYPE, SESSION_ID);
            object.put(SESSION_ID, session.getId());

            session.sendMessage(new TextMessage(object.toJSONString()));
            log.info(object.toJSONString());

        } else {
            throw new CommonException(ErrorCode.ERR_ID_CHATROOM);
        }
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

//        String payload = message.getPayload();
//        log.info("payload : {}", payload);
//
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        System.out.println(chatMessage);
        //ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        ChatRoom chatRoom = repository.getChatRoom(chatMessage.getChatRoomId());
//        chatRoom.handleMessage(session, chatMessage, objectMapper);

        String text = message.getPayload();

        JSONObject object = JsonUtil.parse(text);
        log.info(object.toJSONString());

        if (object.get(CHATROOM_ID) != null) {

            Long chatroomId = (Long) object.get(CHATROOM_ID);

            // 해당 방의 세션에만 메세지 발송
            Map<String, WebSocketSession> sessionMap = chatroomMap.get(chatroomId);
            for (String key : sessionMap.keySet()) {
                WebSocketSession wss = sessionMap.get(key);
                wss.sendMessage(new TextMessage(object.toJSONString()));
            }

            Message msg = Message.builder()
                    .type(MessageType.MESSAGE)
                    .chatroomId(chatroomId)
                    .sessionId(session.getId())
                    .userKey((long) object.get("userKey"))
                     .username((String) object.get(USERNAME))   // 보낸 사람
                    .message((String) object.get(MESSAGE))
                    //.sentAt(LocalDateTime.now())
                    .sentAt(LocalDateTimeUtil.getDateTimeToString(LocalDateTime.now()))
                    .checked(sessionMap.size() == 2 ? true : false)
                    .build();
            
            log.info(msg.toString());
            messageService.saveMessage(msg);

        }
    }

    // 소켓 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("------------ Connection Closed ------------");
        Collection<Map<String, WebSocketSession>> values = chatroomMap.values();
        for (Map<String, WebSocketSession> sessionMap : values) {
            if (sessionMap.containsKey(session.getId())) {
                sessionMap.remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }
}