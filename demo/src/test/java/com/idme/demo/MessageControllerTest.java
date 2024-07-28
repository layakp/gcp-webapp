package com.idme.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    public void testGetMessage_ShouldReturnHelloWorld_WhenNoMessages() throws Exception {
        // Mock the messageService to return a default message
        Mockito.when(messageService.getMessage()).thenReturn("Hello World");

        // Perform a GET request to /api/v1/message
        mockMvc.perform(get("/api/v1/message"))
            .andExpect(status().isOk())  // Expect a 200 OK status code
            .andExpect(content().string(equalTo("Hello World"))); // Expect the response body to be "Hello World"
            
    }

    @Test
    public void testGetMessage_ShouldReturnActualMessage_WhenMessagesExist() throws Exception {
        // Mock the messageService to return a specific message
        String expectedMessage = "This is a message from the service";
        Mockito.when(messageService.getMessage()).thenReturn(expectedMessage);

        // Perform a GET request to /api/v1/message
        mockMvc.perform(get("/api/v1/message"))
            .andExpect(status().isOk())  // Expect a 200 OK status code
            .andExpect(content().string(equalTo(expectedMessage))); // Expect the response body to be the specific message
    }
}

