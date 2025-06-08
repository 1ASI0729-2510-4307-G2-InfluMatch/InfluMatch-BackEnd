package com.influmatch.media.api.dto;

import com.influmatch.media.domain.model.MediaType;
import lombok.Data;

@Data
public class MediaUploadRequest {
    private String base64Content;
    private String filename;
    private String contentType;
    private MediaType type;
    private String description;
    private String title;
} 