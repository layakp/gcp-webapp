package com.idme.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling requests related to messages.
 */
@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    /**
     * Endpoint to retrieve a message.
     * 
     * @return a message string from the MessageService.
     */
    @GetMapping("/message")
    public String getMessage() {
        logger.info("Received request to get message");
        String message = messageService.getMessage();
        logger.info("Returning message: {}", message);
        return message;
    }
}
