package com.influmatch.chat.infrastructure.persistence.repository;

import com.influmatch.chat.domain.model.entity.Chat;
import com.influmatch.chat.domain.model.entity.Message;
import com.influmatch.chat.domain.model.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

    private final JpaChatRepository jpaChatRepository;
    private final JpaMessageRepository jpaMessageRepository;

    @Override
    public List<Chat> findChatsByUserId(Long userId) {
        return jpaChatRepository.findByUserId(userId).stream()
                .map(this::toChatDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Chat> findById(Long chatId) {
        return jpaChatRepository.findById(chatId)
                .map(this::toChatDomain);
    }

    @Override
    public List<Message> findMessagesByChatId(Long chatId) {
        return jpaMessageRepository.findByChatIdOrderByCreatedAtDesc(chatId).stream()
                .map(this::toMessageDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Message saveMessage(Message message) {
        var entity = com.influmatch.chat.infrastructure.persistence.entity.Message.builder()
                .chatId(message.getChatId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .attachmentUrl(message.getAttachmentUrl())
                .createdAt(message.getCreatedAt())
                .build();
        
        return toMessageDomain(jpaMessageRepository.save(entity));
    }

    @Override
    public void updateUnreadCount(Long chatId, Long userId, int unreadCount) {
        jpaChatRepository.updateUnreadCount(chatId, userId, unreadCount);
    }

    @Override
    public Chat save(Chat chat) {
        var entity = com.influmatch.chat.infrastructure.persistence.entity.Chat.builder()
                .userId(chat.getUserId())
                .interlocutorId(chat.getInterlocutorId())
                .unreadCount(chat.getUnreadCount())
                .build();
        
        return toChatDomain(jpaChatRepository.save(entity));
    }

    private Chat toChatDomain(com.influmatch.chat.infrastructure.persistence.entity.Chat entity) {
        // Get the last message for this chat
        var messages = jpaMessageRepository.findByChatIdOrderByCreatedAtDesc(entity.getId());
        Message lastMessage = messages.isEmpty() ? null : toMessageDomain(messages.get(0));

        return Chat.builder()
                .chatId(entity.getId())
                .userId(entity.getUserId())
                .interlocutorId(entity.getInterlocutorId())
                .lastMessage(lastMessage)
                .unreadCount(entity.getUnreadCount())
                .build();
    }

    private Message toMessageDomain(com.influmatch.chat.infrastructure.persistence.entity.Message entity) {
        return Message.builder()
                .messageId(entity.getId())
                .chatId(entity.getChatId())
                .senderId(entity.getSenderId())
                .receiverId(entity.getReceiverId())
                .content(entity.getContent())
                .attachmentUrl(entity.getAttachmentUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
} 