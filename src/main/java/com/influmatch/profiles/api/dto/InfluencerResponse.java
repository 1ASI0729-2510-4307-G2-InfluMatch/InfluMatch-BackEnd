package com.influmatch.profiles.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de perfil de influencer")
public record InfluencerResponse(
    @Schema(description = "ID del perfil", example = "1")
    Long id,

    @Schema(description = "ID del usuario asociado", example = "1")
    Long userId,

    @Schema(description = "Nombre para mostrar", example = "John Doe")
    String displayName,

    @Schema(description = "Biografía", example = "Lifestyle & Tech influencer")
    String bio,

    @Schema(description = "Categoría principal", example = "LIFESTYLE")
    String category,

    @Schema(description = "País", example = "España")
    String country,

    @Schema(description = "Número de seguidores", example = "10000")
    Long followersCount,

    @Schema(description = "Mensaje de la operación", example = "Perfil encontrado exitosamente")
    String message
) {} 