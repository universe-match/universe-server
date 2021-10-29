package com.univer.universerver.source.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.univer.universerver.source.model.*;
import com.univer.universerver.source.model.request.chatroom.HelloMessage;
import com.univer.universerver.source.model.response.ChatRoomResponse;
import com.univer.universerver.source.model.response.MatchRoomResponse;
import com.univer.universerver.source.model.response.MessageResponse;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(HelloMessage message) throws Exception {
        System.out.println(message.toString());
        System.out.println("message====subscribe");
        return "message is ok";
    }
    @MessageMapping("/hello1")
    @SendTo("/topic/greetings1")
    public String greeting1(@Payload HelloMessage message) throws Exception {
        System.out.println("message====12312321"+message.getName());
        return "ban";
    }
    @GetMapping(value="/msg")
    public void SendAPI() {
//        HelloMessage msg = new HelloMessage();
//        msg.setName("goto");

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("test11님이 입장하셨습니다");
        messageResponse.setChatroomId(20L);


        //ObjectMapper mapper = new ObjectMapper();
//        JSONObject msg = new JSONObject();
//        msg.put("name", "제목1");
        messagingTemplate.convertAndSend("/topic/enter/20", messageResponse);
        //webSocket.convertAndSend("/topic/greetings" , msg.toString());
    }
    @GetMapping(value="/test1")
    public String SendAP1I() {
        return "tttt";
    }

}
