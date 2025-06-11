package com.influmatch.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private String content;
    private List<MediaAttachment> media;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MediaAttachment {
        private String attachmentBase64;
        private String attachmentMimeType;
    }
} 