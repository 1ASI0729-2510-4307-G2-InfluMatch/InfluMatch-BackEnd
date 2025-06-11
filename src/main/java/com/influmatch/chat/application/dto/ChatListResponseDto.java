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
@Schema(description = "Chat list item response")
public class ChatListResponseDto {
    
    @Schema(description = "Unique identifier of the chat", example = "123")
    private Long chatId;
    
    @Schema(description = "Unique identifier of the interlocutor", example = "42")
    private Long interlocutorId;
    
    @Schema(description = "Information about the chat interlocutor")
    private InterlocutorInfo interlocutor;
    
    @Schema(description = "Last message in the chat")
    private LastMessageInfo lastMessage;
    
    @Schema(description = "Number of unread messages", example = "5")
    private int unreadCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Interlocutor information")
    public static class InterlocutorInfo {
        @Schema(description = "Unique identifier of the interlocutor", example = "42")
        private Long id;
        
        @Schema(description = "Name of the interlocutor", example = "AnaFit")
        private String name;
        
        @Schema(description = "Profile photo in base64 format", example = "data:image/jpeg;base64,/9j/4AAQ...")
        private String photoBase64;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Last message information")
    public static class LastMessageInfo {
        @Schema(description = "Content of the message", example = "Hello!")
        private String content;
        
        @Schema(description = "When the message was sent")
        private Instant createdAt;
    }
} 