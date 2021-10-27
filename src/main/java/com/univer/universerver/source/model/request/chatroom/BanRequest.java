package com.univer.universerver.source.model.request.chatroom;

import lombok.Getter;

@Getter
public class BanRequest {

    String[] ids;
    long chatroomId;
}
