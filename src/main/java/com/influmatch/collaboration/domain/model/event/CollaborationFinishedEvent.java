package com.influmatch.collaboration.domain.model.event;

import lombok.Getter;

@Getter
public class CollaborationFinishedEvent extends CollaborationEvent {
    private final Long finishedById;

    public CollaborationFinishedEvent(Long collaborationId, Long finishedById) {
        super(collaborationId);
        this.finishedById = finishedById;
    }
} 