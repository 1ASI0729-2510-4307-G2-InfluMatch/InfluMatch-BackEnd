package com.influmatch.chat.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter @Setter
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;

    @Column(nullable = false)
    private Long senderId;  // ID del usuario que envió el mensaje

    @Column(nullable = false, length = 1000)
    private String content;  // Contenido del mensaje

    @Column
    private LocalDateTime readAt;  // Fecha de lectura (null si no leído)
} 