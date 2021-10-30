package com.univer.universerver.source.service;

import com.univer.universerver.source.model.Complain;
import com.univer.universerver.source.repository.ComplainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplainService {
    @Autowired
    private ComplainRepository complainRepository;
    public void saveContent(String content,long userKey) {
        Complain complain =new Complain();
        complain.setContent(content);
        complain.setUserKey(userKey);
        complainRepository.save(complain);
    }
}
