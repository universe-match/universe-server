package com.univer.universerver.source.model.response;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.ChatRoomUser;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomResponse {

    private long id;

    private MatchRoomResponse matchRoomResponse;

    private UserResponse users;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.matchRoomResponse = new MatchRoomResponse(chatRoom.getMatchRoom());
    }
    public ChatRoomResponse(UserResponse user) {
        this.users = user;
    }

    public ChatRoomResponse(ChatRoomUser user) {
        this.users = new UserResponse(user.getUser());
    }
}
