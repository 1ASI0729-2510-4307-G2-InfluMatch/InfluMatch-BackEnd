package com.influmatch.chat.application;

import com.influmatch.chat.domain.event.MessageEvent;
import com.influmatch.chat.domain.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public void publishMessageCreated(Message message) {
        MessageEvent event = MessageEvent.created(message);
        String destination = String.format("/topic/dialogs/%d/messages", message.getDialog().getId());
        messagingTemplate.convertAndSend(destination, event);
    }

    public void publishMessageRead(Message message) {
        MessageEvent event = MessageEvent.read(message);
        String destination = String.format("/topic/dialogs/%d/messages", message.getDialog().getId());
        messagingTemplate.convertAndSend(destination, event);
    }

    public void publishMessageDeleted(Long dialogId, Long messageId) {
        MessageEvent event = MessageEvent.deleted(dialogId, messageId);
        String destination = String.format("/topic/dialogs/%d/messages", dialogId);
        messagingTemplate.convertAndSend(destination, event);
    }
} 