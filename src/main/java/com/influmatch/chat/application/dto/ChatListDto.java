package com.influmatch.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatListDto {
    private Long chatId;
    private InterlocutorDto interlocutor;
    private LastMessageDto lastMessage;
    private int unreadCount;
} 