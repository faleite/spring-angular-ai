package com.faleite.api_ai.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping
    ResponseEntity<?> simpleChat(@RequestBody ChatMessage message) {
        String response = this.chatClient.prompt()
                .user(message.message())
                .call()
                .content();
        return ResponseEntity.ok(response);
    }
}
