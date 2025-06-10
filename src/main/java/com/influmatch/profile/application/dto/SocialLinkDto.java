package com.influmatch.profile.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos de un enlace a red social")
public class SocialLinkDto {
    @Schema(description = "Plataforma de red social", example = "Instagram")
    @NotBlank(message = "Platform is required")
    private String platform;

    @Schema(description = "URL del perfil social", example = "https://www.instagram.com/username")
    @NotBlank(message = "URL is required")
    @URL(message = "Social media URL must be a valid URL")
    private String url;
} 