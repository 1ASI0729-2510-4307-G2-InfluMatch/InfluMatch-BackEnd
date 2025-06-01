package com.influmatch.chat.application;

import com.influmatch.chat.domain.event.DialogEvent;
import com.influmatch.chat.domain.model.Dialog;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DialogEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String DIALOG_EVENTS_TOPIC = "/topic/dialogs";

    public void publishDialogCreated(Dialog dialog) {
        DialogEvent event = DialogEvent.created(dialog);
        messagingTemplate.convertAndSend(DIALOG_EVENTS_TOPIC, event);
    }

    public void publishDialogDeleted(Long dialogId) {
        DialogEvent event = DialogEvent.deleted(dialogId);
        messagingTemplate.convertAndSend(DIALOG_EVENTS_TOPIC, event);
    }
} 