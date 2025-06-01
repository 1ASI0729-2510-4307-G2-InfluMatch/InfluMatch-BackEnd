package com.influmatch.chat.domain.event;

import lombok.Value;

import java.time.Instant;

@Value
public class MessageEventPayload {
    Long messageId;
    Long dialogId;
    Long senderId;
    String content;
    Long assetId;
    Instant createdAt;
    Instant readAt;
} 