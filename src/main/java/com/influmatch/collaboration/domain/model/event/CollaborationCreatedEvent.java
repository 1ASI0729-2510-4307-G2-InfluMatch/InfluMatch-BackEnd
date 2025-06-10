package com.influmatch.collaboration.domain.model.event;

import com.influmatch.auth.domain.model.UserRole;
import lombok.Getter;

@Getter
public class CollaborationCreatedEvent extends CollaborationEvent {
    private final Long initiatorId;
    private final Long counterpartId;
    private final UserRole initiatorRole;

    public CollaborationCreatedEvent(Long collaborationId, Long initiatorId, Long counterpartId, UserRole initiatorRole) {
        super(collaborationId);
        this.initiatorId = initiatorId;
        this.counterpartId = counterpartId;
        this.initiatorRole = initiatorRole;
    }
} 