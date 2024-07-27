package com.idme.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @PostConstruct
    public void init() {
        // Initialize the database with a "hello world" message
        if (messageRepository.count() == 0) {
            messageRepository.save(new MessageDTO("hello world"));
        }
    }

    public String getMessage() {
        return messageRepository.findAll().get(0).getContent();
    }
}

