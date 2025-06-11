package com.influmatch.chat.domain.model.repository;

import com.influmatch.chat.domain.model.entity.Chat;
import com.influmatch.chat.domain.model.entity.Message;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    List<Chat> findChatsByUserId(Long userId);
    Optional<Chat> findById(Long chatId);
    List<Message> findMessagesByChatId(Long chatId);
    Message saveMessage(Message message);
    void updateUnreadCount(Long chatId, Long userId, int unreadCount);
    Chat save(Chat chat);
} 