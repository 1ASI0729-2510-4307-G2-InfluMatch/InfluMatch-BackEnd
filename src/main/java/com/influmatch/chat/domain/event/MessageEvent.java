package com.influmatch.chat.domain.event;

import com.influmatch.chat.domain.model.Message;
import lombok.Getter;

@Getter
public class MessageEvent {
    private final String type;
    private final MessageEventPayload payload;

    private MessageEvent(String type, MessageEventPayload payload) {
        this.type = type;
        this.payload = payload;
    }

    public static MessageEvent created(Message message) {
        return new MessageEvent(
            "messageCreated",
            new MessageEventPayload(
                message.getId(),
                message.getDialog().getId(),
                message.getSenderId(),
                message.getContent(),
                message.getAssetId(),
                message.getCreatedAt(),
                null
            )
        );
    }

    public static MessageEvent read(Message message) {
        return new MessageEvent(
            "messageRead",
            new MessageEventPayload(
                message.getId(),
                message.getDialog().getId(),
                message.getSenderId(),
                message.getContent(),
                message.getAssetId(),
                message.getCreatedAt(),
                message.getReadAt()
            )
        );
    }

    public static MessageEvent deleted(Long dialogId, Long messageId) {
        return new MessageEvent(
            "messageDeleted",
            new MessageEventPayload(messageId, dialogId, null, null, null, null, null)
        );
    }
} 