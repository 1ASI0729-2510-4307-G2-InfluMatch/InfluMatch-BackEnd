package com.influmatch.collaboration.domain.model.event;

import lombok.Getter;

@Getter
public class CollaborationAcceptedEvent extends CollaborationEvent {
    private final Long acceptedById;

    public CollaborationAcceptedEvent(Long collaborationId, Long acceptedById) {
        super(collaborationId);
        this.acceptedById = acceptedById;
    }
} 