package com.influmatch.profiles.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear/actualizar perfil de influencer")
public record InfluencerRequest(
    @Schema(
        description = "Nombre para mostrar",
        example = "John Doe"
    )
    @Size(min = 3, max = 50, message = "display_name_length")
    String displayName,

    @Schema(
        description = "Biografía",
        example = "Lifestyle & Tech influencer"
    )
    @Size(max = 255, message = "bio_length")
    String bio,

    @Schema(
        description = "Categoría principal",
        example = "LIFESTYLE"
    )
    @Size(max = 50, message = "category_length")
    String category,

    @Schema(
        description = "País",
        example = "España"
    )
    @Size(max = 50, message = "country_length")
    String country,

    @Schema(
        description = "Número de seguidores",
        example = "10000"
    )
    Long followersCount
) {} 