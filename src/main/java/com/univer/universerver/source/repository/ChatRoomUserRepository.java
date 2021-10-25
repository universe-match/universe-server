package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.ChatRoomUser;
import com.univer.universerver.source.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
    void deleteByUserIdAndChatRoomUserId(long userId, long id);

    ChatRoomUser findByUserIdAndChatRoomUserId(long userId, long id);
}
