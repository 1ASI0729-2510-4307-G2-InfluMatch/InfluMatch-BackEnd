package com.influmatch.profiles.api.dto;

import com.influmatch.media.api.dto.MediaUploadRequest;
import lombok.Data;

@Data
public class CreateInfluencerProfileRequest {
    private String displayName;
    private String bio;
    private String category;
    private String country;
    private Long followersCount;
    private MediaUploadRequest profilePicture;
} 