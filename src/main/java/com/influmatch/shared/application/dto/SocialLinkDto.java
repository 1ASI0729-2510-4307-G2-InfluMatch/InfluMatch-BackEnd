package com.influmatch.shared.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Enlace a red social")
public class SocialLinkDto {
    @Schema(description = "Plataforma de red social")
    private String platform;

    @Schema(description = "URL del perfil social")
    private String url;
} 