package com.influmatch.chat.domain.model;

import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "dialogs")
@Getter
@NoArgsConstructor
public class Dialog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;  // Opcional: diálogo asociado a una campaña

    @ElementCollection
    @CollectionTable(
        name = "dialog_participants",
        joinColumns = @JoinColumn(name = "dialog_id")
    )
    @Column(name = "participant_id")
    private Set<Long> participantIds = new HashSet<>();  // IDs de usuarios participantes

    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Message> messages = new ArrayList<>();  // Mensajes ordenados por fecha

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Dialog(Long campaignId, HashSet<Long> participantIds) {
        this.participantIds = participantIds;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
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

    // Método helper para agregar participante
    public void addParticipant(Long userId) {
        participantIds.add(userId);
    }

    // Método helper para verificar participante
    public boolean isParticipant(Long userId) {
        return participantIds.contains(userId);
    }

    // Método helper para verificar si hay mensajes no leídos
    public boolean hasUnreadMessages() {
        return messages.stream()
            .anyMatch(message -> message.getReadAt() == null);
    }
} 