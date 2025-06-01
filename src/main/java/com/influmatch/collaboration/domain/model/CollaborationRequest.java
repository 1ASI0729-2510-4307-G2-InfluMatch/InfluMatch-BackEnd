package com.influmatch.collaboration.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collaboration_requests")
@Getter @Setter
public class CollaborationRequest extends BaseEntity {

    @Column(nullable = false)
    private Long fromUserId;  // ID del usuario que envía la solicitud

    @Column(nullable = false)
    private Long toUserId;    // ID del usuario que recibe la solicitud

    @Column(length = 1000)
    private String message;   // Mensaje opcional de la solicitud

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollaborationStatus status = CollaborationStatus.PENDING;
}
