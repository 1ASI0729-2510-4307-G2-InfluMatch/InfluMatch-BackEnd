package com.influmatch.profile.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInfluencerProfileRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotEmpty(message = "At least one niche is required")
    @Size(min = 1, max = 5, message = "Number of niches must be between 1 and 5")
    @Builder.Default
    private Set<@NotBlank(message = "Niche cannot be blank") String> niches = new HashSet<>();

    @NotBlank(message = "Bio is required")
    @Size(min = 50, max = 1000, message = "Bio must be between 50 and 1000 characters")
    private String bio;

    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country must be a valid ISO 3166-1 alpha-2 code")
    private String country;

    private String photo;  // Base64 or null if using multipart

    private String profilePhoto;  // Base64 or null if using multipart

    @NotNull(message = "Followers count is required")
    @Min(value = 0, message = "Followers must be a non-negative number")
    private Integer followers;

    @Valid
    @Builder.Default
    private List<SocialLinkDto> socialLinks = new ArrayList<>();

    private String location;

    @Valid
    @Builder.Default
    private List<LinkDto> links = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<AttachmentDto> attachments = new ArrayList<>();
} 