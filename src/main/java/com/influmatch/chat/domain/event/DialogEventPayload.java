package com.influmatch.chat.domain.event;

import lombok.Value;

import java.time.Instant;
import java.util.Set;

@Value
public class DialogEventPayload {
    Long dialogId;
    Long campaignId;
    Set<Long> participants;
    Instant createdAt;
} 