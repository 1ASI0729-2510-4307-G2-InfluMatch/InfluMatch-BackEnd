package com.influmatch.chat.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long messageId;
    private Long chatId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String attachmentUrl;
    private Instant createdAt;
} 