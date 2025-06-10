package com.influmatch.dashboard.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos resumidos de un influencer para el listado")
public class DashboardInfluencerListDto {
    @Schema(description = "ID del usuario del influencer")
    private Long userId;

    @Schema(description = "Nombre del influencer")
    private String name;

    @Schema(description = "Biografía del influencer")
    private String bio;

    @Schema(description = "URL de la foto del influencer en base64")
    private String photoUrl;

    @Schema(description = "País del influencer")
    private String country;

    @Schema(description = "Nicho principal del influencer")
    private String mainNiche;

    @Schema(description = "Número de seguidores")
    private Long followersCount;
} 