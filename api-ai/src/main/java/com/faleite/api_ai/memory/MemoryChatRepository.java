package com.faleite.api_ai.memory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MemoryChatRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemoryChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generateChatId(String userId, String description){
        final String sql = "INSERT INTO CHAT_MEMORY (conversation_id, user_id, description) VALUES (?, ?, ?)";
        String conversation_id = UUID.randomUUID().toString();
        jdbcTemplate.update(sql, conversation_id, userId, description);
        return conversation_id;
    }

    public List<Chat> getAllChatsForUser(String userId){
        final String sql = "SELECT conversation_id, user_id, description FROM CHAT_MEMORY WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, int_) ->
            new Chat(rs.getString("conversation_id"),  rs.getString("description")), userId);
    }

    public List<ChatMessage> getChatMessages(String chatId){
        final String sql = "SELECT content, type FROM SPRING_AI_CHAT_MEMORY WHERE conversation_id = ? ORDER BY timestamp ASC";
        return jdbcTemplate.query(sql, (rs, int_) ->
                new ChatMessage(rs.getString("content"), rs.getString("type")), chatId);
    }
}
