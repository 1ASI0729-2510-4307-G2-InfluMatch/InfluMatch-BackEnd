package com.influmatch.collaboration.domain.model.event;

import com.influmatch.shared.domain.model.DomainEvent;
import lombok.Getter;

@Getter
public abstract class CollaborationEvent extends DomainEvent {
    private final Long collaborationId;

    protected CollaborationEvent(Long collaborationId) {
        this.collaborationId = collaborationId;
    }
} 