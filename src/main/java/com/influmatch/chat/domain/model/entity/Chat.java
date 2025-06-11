package com.influmatch.chat.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    private Long chatId;
    private Long userId;
    private Long interlocutorId;
    private Message lastMessage;
    private int unreadCount;
} 