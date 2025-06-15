package com.influmatch.collaboration.domain.model.entity;

import com.influmatch.auth.domain.model.UserRole;
import com.influmatch.collaboration.domain.model.valueobject.ActionType;
import com.influmatch.collaboration.domain.model.valueobject.CollaborationStatus;
import com.influmatch.collaboration.domain.model.valueobject.Milestone;
import com.influmatch.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Collaboration extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initiator_id", nullable = false)
    private Long initiatorId;

    @Column(name = "counterpart_id", nullable = false)
    private Long counterpartId;

    @Enumerated(EnumType.STRING)
    @Column(name = "initiator_role", nullable = false)
    private UserRole initiatorRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollaborationStatus status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal budget;

    @ElementCollection
    @CollectionTable(
        joinColumns = @JoinColumn(name = "collaboration_id")
    )
    private List<Milestone> milestones = new ArrayList<>();

    private String location;

    private String deliverables;

    public Collaboration(
            Long initiatorId,
            Long counterpartId,
            UserRole initiatorRole,
            String message,
            ActionType actionType,
            LocalDate targetDate,
            BigDecimal budget,
            List<Milestone> milestones,
            String location,
            String deliverables
    ) {
        if (initiatorId == null) {
            throw new IllegalArgumentException("Initiator ID cannot be null");
        }
        if (counterpartId == null) {
            throw new IllegalArgumentException("Counterpart ID cannot be null");
        }
        if (initiatorRole == null) {
            throw new IllegalArgumentException("Initiator role cannot be null");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        if (actionType == null) {
            throw new IllegalArgumentException("Action type cannot be null");
        }
        if (targetDate == null) {
            throw new IllegalArgumentException("Target date cannot be null");
        }
        if (budget == null || budget.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget must be greater than zero");
        }

        this.initiatorId = initiatorId;
        this.counterpartId = counterpartId;
        this.initiatorRole = initiatorRole;
        this.status = CollaborationStatus.PENDING;
        this.message = message.trim();
        this.actionType = actionType;
        this.targetDate = targetDate;
        this.budget = budget;
        this.milestones = milestones != null ? new ArrayList<>(milestones) : new ArrayList<>();
        this.location = location != null ? location.trim() : null;
        this.deliverables = deliverables != null ? deliverables.trim() : null;
    }

    public void accept() {
        validateStatusIs(CollaborationStatus.PENDING);
        this.status = CollaborationStatus.ACCEPTED;
    }

    public void reject() {
        validateStatusIs(CollaborationStatus.PENDING);
        this.status = CollaborationStatus.REJECTED;
    }

    public void finish() {
        validateStatusIs(CollaborationStatus.ACCEPTED);
        this.status = CollaborationStatus.FINISHED;
    }

    public void cancel() {
        validateStatusIs(CollaborationStatus.PENDING);
        this.status = CollaborationStatus.CANCELED;
    }

    public void update(String message, LocalDate targetDate, ActionType actionType, BigDecimal budget,
                      List<Milestone> milestones, String location, String deliverables) {
        validateStatusIs(CollaborationStatus.PENDING);

        if (message != null && !message.trim().isEmpty()) {
            this.message = message.trim();
        }
        if (targetDate != null) {
            this.targetDate = targetDate;
        }
        if (actionType != null) {
            this.actionType = actionType;
        }
        if (budget != null && budget.compareTo(BigDecimal.ZERO) > 0) {
            this.budget = budget;
        }
        if (milestones != null) {
            this.milestones = new ArrayList<>(milestones);
        }
        if (location != null) {
            this.location = location.trim();
        }
        if (deliverables != null) {
            this.deliverables = deliverables.trim();
        }
    }

    private void validateStatusIs(CollaborationStatus expectedStatus) {
        if (this.status != expectedStatus) {
            throw new IllegalStateException("Collaboration must be in " + expectedStatus + " state");
        }
    }
} 