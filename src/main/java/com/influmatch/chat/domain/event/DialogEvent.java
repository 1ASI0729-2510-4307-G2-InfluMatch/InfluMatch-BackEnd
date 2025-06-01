package com.influmatch.chat.domain.event;

import com.influmatch.chat.domain.model.Dialog;
import lombok.Getter;

@Getter
public class DialogEvent {
    private final String type;
    private final DialogEventPayload payload;

    private DialogEvent(String type, DialogEventPayload payload) {
        this.type = type;
        this.payload = payload;
    }

    public static DialogEvent created(Dialog dialog) {
        return new DialogEvent(
            "dialogCreated",
            new DialogEventPayload(
                dialog.getId(),
                dialog.getCampaign() != null ? dialog.getCampaign().getId() : null,
                dialog.getParticipantIds(),
                dialog.getCreatedAt()
            )
        );
    }

    public static DialogEvent deleted(Long dialogId) {
        return new DialogEvent(
            "dialogDeleted",
            new DialogEventPayload(dialogId, null, null, null)
        );
    }
} 