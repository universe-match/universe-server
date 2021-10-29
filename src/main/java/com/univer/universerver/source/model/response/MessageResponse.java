package com.univer.universerver.source.model.response;

import com.univer.universerver.source.model.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private Long chatroomId;
    private long userKey;
    private String username;
    private String message;
    private String sentAt;
    private String profileUrl;

    public MessageResponse(){

    }
    public MessageResponse(Message item) {
        this.chatroomId = item.getChatroomId();
        this.userKey = item.getUserKey();
        this.username = item.getUsername();
        this.message = item.getMessage();
        this.sentAt = item.getSentAt();
        this.profileUrl = item.getProfileUrl();
    }
}
