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
@Schema(description = "Detailed message information")
public class MessageDetailDto {
    @Schema(description = "Unique identifier of the message", example = "789")
    private Long messageId;
    
    @Schema(description = "ID of the user who sent the message", example = "10")
    private Long senderId;
    
    @Schema(description = "ID of the user who receives the message", example = "16")
    private Long receiverId;
    
    @Schema(description = "Text content of the message", example = "¡Hola! ¿Te interesa colaborar?")
    private String content;
    
    @Schema(description = "URL of the attached file, if any", example = "https://storage.com/files/attachment.jpg")
    private String attachmentUrl;
    
    @Schema(description = "Timestamp when the message was created", example = "2025-06-15T10:35:00Z")
    private Instant createdAt;
    
    @Schema(description = "Indicates if the message was sent by the current user", example = "true")
    private Boolean isFromMe;
} 