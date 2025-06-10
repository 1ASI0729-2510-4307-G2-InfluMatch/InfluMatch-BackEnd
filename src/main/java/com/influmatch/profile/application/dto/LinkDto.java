package com.influmatch.profile.application.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
    @NotBlank(message = "Link title is required")
    private String title;

    @NotBlank(message = "Link URL is required")
    @URL(message = "Link URL must be a valid URL")
    private String url;
} 