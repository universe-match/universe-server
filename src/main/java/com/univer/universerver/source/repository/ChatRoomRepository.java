package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    List<ChatRoom> findAllByMatchRoomMatchingListUserUserid(String userId);

    List<ChatRoom> findAllByMatchRoomMatchingListUser(User user);

    ChatRoom findByMatchRoom(MatchRoom matchRoom);
}
