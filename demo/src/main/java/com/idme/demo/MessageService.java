package com.idme.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for managing messages.
 */
@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Initializes the database with a "hello world" message if empty.
     */
    @PostConstruct
    public void init() {
        if (messageRepository.count() == 0) {
            messageRepository.save(new MessageDTO("Hello World"));
            logger.info("Initialized database with 'Hello World' message");
        }
    }

    /**
     * Retrieves the first message from the repository.
     *
     * @return the content of the first message
     */
    public String getMessage() {
        return messageRepository.findAll().get(0).getContent();
    }
}
