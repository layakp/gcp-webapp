package com.idme.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public String getMessage() {
        return messageRepository.findAll().get(0).getContent();
    }
}

