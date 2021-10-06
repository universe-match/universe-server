package com.univer.universerver.source.model.response;

import com.univer.universerver.source.model.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponse {

    private long id;

    private MatchRoomResponse matchRoomResponse;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.matchRoomResponse = new MatchRoomResponse(chatRoom.getMatchRoom());
    }
}
