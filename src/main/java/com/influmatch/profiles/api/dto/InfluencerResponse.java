package com.influmatch.profiles.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con datos de perfil de influencer")
public record InfluencerResponse(
    @Schema(description = "ID del perfil", example = "1")
    Long id,

    @Schema(description = "ID del usuario", example = "1")
    Long userId,

    @Schema(description = "Nombre a mostrar", example = "John Doe")
    String displayName,

    @Schema(description = "Biografía", example = "Lifestyle influencer")
    String bio,

    @Schema(description = "Categoría", example = "LIFESTYLE")
    String category,

    @Schema(description = "País", example = "España")
    String country,

    @Schema(description = "Número de seguidores", example = "10000")
    Long followersCount,

    @Schema(description = "ID de la foto de perfil", example = "1")
    Long profilePictureId,

    @Schema(description = "Mensaje de la operación", example = "Operación exitosa")
    String message
) {} 