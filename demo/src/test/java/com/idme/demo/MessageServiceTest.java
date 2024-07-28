package com.idme.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Test
    public void testGetMessage_ShouldReturnHelloWorld_WhenEmptyDatabase() {
      

        when(messageRepository.count()).thenReturn(0L);

    // Call init method directly
    messageService.init();

    // Verify save method was called
    verify(messageRepository).save(any(MessageDTO.class));
    }

    @Test
    public void testGetMessage_ShouldReturnFirstMessage_WhenDatabaseHasData() {
      
            // Mock findAll to return a list with a single item
    List<MessageDTO> messageList = new ArrayList<>();
    messageList.add(new MessageDTO("Existing Message"));
    when(messageRepository.findAll()).thenReturn(messageList);

    // Call the method
    String message = messageService.getMessage();

    // Assertions
    assertEquals("Existing Message", message);
    verify(messageRepository).findAll(); // Ensure findAll() is called
    }
}
