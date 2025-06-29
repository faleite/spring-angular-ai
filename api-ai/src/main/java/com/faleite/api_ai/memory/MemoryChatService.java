package com.faleite.api_ai.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class MemoryChatService {

    private final ChatClient chatClient;

    public MemoryChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory){
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

        InMemoryChatMemoryRepository a;
    }

    public String simpleChat(String message) {
        return this.chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
