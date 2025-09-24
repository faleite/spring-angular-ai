package com.faleite.api_ai.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class MemoryChatService {

    private final ChatClient chatClient;

    private final MemoryChatRepository memoryChatRepository;

    private static final String USER_ID = "fabricio";
    private static final String DESCRIPTION_PROMPT = "Generate a chat description based on the message, " +
            "limiting the description to 30 characters: ";

    public MemoryChatService(ChatClient.Builder chatClientBuilder, JdbcChatMemoryRepository jdbcChatMemoryRepository,
                             MemoryChatRepository memoryChatRepository) {

        this.memoryChatRepository = memoryChatRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
                new SimpleLoggerAdvisor())
                .build();
    }

    public String chat(String message, String chatID) {
        return this.chatClient.prompt()
//                .advisors( a -> a.param(ChatMemory.CONVERSATION_ID, "123456"))
                .user(message)
                .call()
                .content();
    }

    record NewChatResponse(String chatId, String description) {}

    public NewChatResponse createNewChat(String message){
        String description = generateDescription(message);
        String chatId = this.memoryChatRepository.generateChatId(USER_ID, description);
        // prompt the chat client to create a new chat with the generated id

        return new NewChatResponse(chatId, description);
    }

    private String generateDescription(String message){
        return this.chatClient.prompt()
                .user(DESCRIPTION_PROMPT + message)
                .call()
                .content();
    }
}