package com.influmatch.chat.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id", nullable = false)
    @NotNull(message = "El diálogo es requerido")
    private Dialog dialog;

    @Column(name = "sender_id", nullable = false)
    @NotNull(message = "El remitente es requerido")
    private Long senderId;

    @Column(name = "content", nullable = false, length = 1000)
    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    @Size(max = 1000, message = "El contenido del mensaje no puede exceder los 1000 caracteres")
    private String content;

    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "read_at")
    private Instant readAt;

    public Message(Dialog dialog, Long senderId, String content, Long assetId) {
        this.dialog = dialog;
        this.senderId = senderId;
        this.content = content;
        this.assetId = assetId;
        this.createdAt = Instant.now();
    }

    public void markAsRead() {
        if (this.readAt == null) {
            this.readAt = Instant.now();
        }
    }

    public boolean canBeDeletedBy(Long userId) {
        return this.senderId.equals(userId) && (this.readAt == null);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
} 