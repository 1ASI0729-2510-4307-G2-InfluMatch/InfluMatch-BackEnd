package com.influmatch.chat.application.assembler;

import com.influmatch.chat.application.dto.*;
import com.influmatch.chat.domain.model.entity.Chat;
import com.influmatch.chat.domain.model.entity.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatAssembler {
    
    public ChatListDto toDto(Chat chat) {
        return ChatListDto.builder()
                .chatId(chat.getChatId())
                .interlocutor(InterlocutorDto.builder()
                        .id(chat.getInterlocutorId())
                        .name("") // To be filled by user service
                        .photoUrl("") // To be filled by user service
                        .build())
                .lastMessage(chat.getLastMessage() != null ? 
                        LastMessageDto.builder()
                                .content(chat.getLastMessage().getContent())
                                .createdAt(chat.getLastMessage().getCreatedAt())
                                .build() : null)
                .unreadCount(chat.getUnreadCount())
                .build();
    }

    public List<ChatListDto> toDtoList(List<Chat> chats) {
        return chats.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .messageId(message.getMessageId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .attachmentUrl(message.getAttachmentUrl())
                .createdAt(message.getCreatedAt())
                .build();
    }

    public List<MessageDto> toMessageDtoList(List<Message> messages) {
        return messages.stream()
                .map(this::toMessageDto)
                .collect(Collectors.toList());
    }
} 