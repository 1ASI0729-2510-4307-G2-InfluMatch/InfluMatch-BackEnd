package com.influmatch.profiles.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con datos de perfil de marca")
public record BrandResponse(
    @Schema(description = "ID del perfil", example = "1")
    Long id,

    @Schema(description = "ID del usuario", example = "1")
    Long userId,

    @Schema(description = "Nombre de la empresa", example = "Nike")
    String companyName,

    @Schema(description = "Descripción", example = "Empresa líder en artículos deportivos")
    String description,

    @Schema(description = "Industria", example = "Deportes")
    String industry,

    @Schema(description = "URL del sitio web", example = "https://www.nike.com")
    String websiteUrl,

    @Schema(description = "URL del logo", example = "https://example.com/logo.png")
    String logoUrl,

    @Schema(description = "ID de la foto de perfil", example = "1")
    Long profilePictureId,

    @Schema(description = "Mensaje de la operación", example = "Operación exitosa")
    String message
) {} 