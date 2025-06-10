package com.influmatch.collaboration.domain.service;

import com.influmatch.auth.domain.model.User;
import com.influmatch.collaboration.domain.model.entity.Collaboration;
import com.influmatch.collaboration.domain.model.event.CollaborationAcceptedEvent;
import com.influmatch.collaboration.domain.model.event.CollaborationCreatedEvent;
import com.influmatch.collaboration.domain.model.event.CollaborationFinishedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollaborationDomainService {
    private final ApplicationEventPublisher eventPublisher;

    public void validateRoles(User initiator, User counterpart) {
        if (initiator.getRole() == counterpart.getRole()) {
            throw new IllegalArgumentException("Cannot collaborate with user of the same role");
        }
    }

    public void publishCreatedEvent(Collaboration collaboration) {
        eventPublisher.publishEvent(new CollaborationCreatedEvent(
                collaboration.getId(),
                collaboration.getInitiatorId(),
                collaboration.getCounterpartId(),
                collaboration.getInitiatorRole()
        ));
    }

    public void publishAcceptedEvent(Collaboration collaboration, Long acceptedById) {
        eventPublisher.publishEvent(new CollaborationAcceptedEvent(
                collaboration.getId(),
                acceptedById
        ));
    }

    public void publishFinishedEvent(Collaboration collaboration, Long finishedById) {
        eventPublisher.publishEvent(new CollaborationFinishedEvent(
                collaboration.getId(),
                finishedById
        ));
    }
} 