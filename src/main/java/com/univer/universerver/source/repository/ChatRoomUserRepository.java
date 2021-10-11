package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
}
