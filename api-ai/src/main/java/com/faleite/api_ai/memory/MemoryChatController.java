package com.faleite.api_ai.memory;

import com.faleite.api_ai.chat.ChatMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-memory")
public class MemoryChatController {

    private final MemoryChatService chatService;

    public MemoryChatController(MemoryChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    ChatMessage simpleChat(@RequestBody ChatMessage chatMessage) {
        String response = this.chatService.simpleChat(chatMessage.message());
        return new ChatMessage(response);
    }
}
