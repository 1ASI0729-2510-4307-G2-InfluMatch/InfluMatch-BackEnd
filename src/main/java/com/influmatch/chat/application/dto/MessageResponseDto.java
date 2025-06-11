package com.influmatch.chat.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Message response data")
public class MessageResponseDto {
    @Schema(description = "Unique identifier of the message", example = "456")
    private Long messageId;
    
    @Schema(description = "ID of the chat this message belongs to", example = "123")
    private Long chatId;
    
    @Schema(description = "ID of the user who sent the message", example = "10")
    private Long senderId;
    
    @Schema(description = "ID of the user who receives the message", example = "42")
    private Long receiverId;
    
    @Schema(description = "Text content of the message", example = "Hola, ¿cómo estás?")
    private String content;
    
    @Schema(description = "URL of the attached file, if any", example = "https://…/files/456.png")
    private String attachmentUrl;
    
    @Schema(description = "Timestamp when the message was created", example = "2025-06-11T09:00:05Z")
    private Instant createdAt;
} 