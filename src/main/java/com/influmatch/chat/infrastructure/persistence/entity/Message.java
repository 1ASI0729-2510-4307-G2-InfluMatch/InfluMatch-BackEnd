package com.influmatch.chat.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long chatId;
    
    private Long senderId;
    
    private Long receiverId;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String attachmentUrl;
    
    private Instant createdAt;
} 