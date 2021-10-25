package com.univer.universerver.source.service;

import com.univer.universerver.source.model.ChatRoom;
import com.univer.universerver.source.model.ChatRoomUser;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.repository.ChatRoomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatRoomUserService {

    @Autowired
    private ChatRoomUserRepository chatRoomUserRepository;

    public void insertChatRoomUser(ChatRoom chatRoom, User user) {

        ChatRoomUser chatRoomUser = new ChatRoomUser();
        chatRoomUser.setChatRoomUser(chatRoom);
        chatRoomUser.setUser(user);
        chatRoomUserRepository.save(chatRoomUser);
    }

    public void deleteUser(User user, long chatroomId) {
        long userId = user.getId();

        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByUserIdAndChatRoomUserId(userId,chatroomId);
        chatRoomUserRepository.deleteById(chatRoomUser.getId());
    }
}
