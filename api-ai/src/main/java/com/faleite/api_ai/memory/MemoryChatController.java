package com.faleite.api_ai.memory;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-memory")
public class MemoryChatController {

    private final MemoryChatService chatService;

    public MemoryChatController(MemoryChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{chatId}")
    ChatMessage simpleChat(@PathVariable String chatId, @RequestBody ChatMessage message) {
        String response = chatService.chat(message.content(), chatId);
        return new ChatMessage(response, "ASSISTANT");
    }

    @PostMapping("/start")
    NewChatResponse startNewChat(@RequestBody ChatMessage message) {
        return this.chatService.createNewChat(message.content());
    }

    @GetMapping
    List<Chat> getAllChatsForUser(){
        return this.chatService.getAllChatsForUser();
    }

    @GetMapping("/{chatId}")
    List<ChatMessage> getChatMessages(@PathVariable String chatId){
        return this.chatService.getChatMessages(chatId);
    }
}
