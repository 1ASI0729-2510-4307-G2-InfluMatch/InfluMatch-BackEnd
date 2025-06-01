package com.influmatch.ratings.domain.model;

import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "ratings")
@Getter
@NoArgsConstructor
public class Rating extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "writer_id", nullable = false)
    @NotNull(message = "El ID del autor es requerido")
    private Long writerId;

    @Column(name = "target_id", nullable = false)
    @NotNull(message = "El ID del destinatario es requerido")
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(name = "score", nullable = false)
    @Min(value = 1, message = "La puntuación debe ser al menos 1")
    @Max(value = 5, message = "La puntuación no puede ser mayor a 5")
    private Integer score;

    @Column(name = "comment", length = 1000)
    @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres")
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Rating(Long writerId, Long targetId, Campaign campaign, Integer score, String comment) {
        this.writerId = writerId;
        this.targetId = targetId;
        this.campaign = campaign;
        this.score = score;
        this.comment = comment;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void update(Integer score, String comment) {
        if (score != null) {
            this.score = score;
        }
        if (comment != null) {
            this.comment = comment;
        }
        this.updatedAt = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
