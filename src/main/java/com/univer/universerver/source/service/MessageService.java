package com.univer.universerver.source.service;

import com.univer.universerver.source.model.Message;
import com.univer.universerver.source.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class MessageService {

	@Autowired
    private MessageRepository messageRepository;

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    public List<Message> selectChatroomInfo(long id) {
        return messageRepository.findByChatroomId(id);
    }
}
