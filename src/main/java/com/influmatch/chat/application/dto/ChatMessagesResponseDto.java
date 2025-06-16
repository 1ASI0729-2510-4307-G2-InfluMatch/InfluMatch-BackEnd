package com.influmatch.chat.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Complete chat messages response with interlocutor info")
public class ChatMessagesResponseDto {
    
    @Schema(description = "Information about the interlocutor")
    private InterlocutorInfo interlocutor;
    
    @Schema(description = "List of messages in the chat")
    private List<MessageDetailDto> messages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Interlocutor information")
    public static class InterlocutorInfo {
        @Schema(description = "ID of the interlocutor", example = "16")
        private Long userId;
        
        @Schema(description = "Name of the interlocutor", example = "Carlos Tech")
        private String name;
        
        @Schema(description = "Photo of the interlocutor in base64", example = "data:image/jpeg;base64,/9j/4AAQ...")
        private String photoBase64;
    }
} 