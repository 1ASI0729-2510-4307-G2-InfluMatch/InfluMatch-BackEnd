package com.influmatch.profiles.api.dto;

import com.influmatch.media.api.dto.MediaUploadRequest;
import lombok.Data;

@Data
public class CreateBrandProfileRequest {
    private String companyName;
    private String description;
    private String industry;
    private String websiteUrl;
    private String logoUrl;
    private MediaUploadRequest profilePicture;
} 