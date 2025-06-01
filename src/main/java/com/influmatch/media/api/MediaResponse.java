package com.influmatch.media.api;

import com.influmatch.media.domain.model.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Detalles de un activo multimedia")
public record MediaResponse(
    @Schema(description = "ID único del activo", example = "1")
    Long id,

    @Schema(description = "Nombre del archivo", example = "video-promo-2024.mp4")
    String filename,

    @Schema(description = "URL para acceder al archivo", example = "https://storage.influmatch.com/media/video-promo-2024.mp4")
    String url,

    @Schema(description = "Tipo de contenido", example = "video/mp4")
    String contentType,

    @Schema(description = "Tamaño en bytes", example = "1048576")
    Long size,

    @Schema(description = "Tipo de medio", example = "VIDEO", allowableValues = {"VIDEO", "IMAGE", "DOCUMENT"})
    MediaType type,

    @Schema(description = "Título del activo", example = "Video promocional verano 2024")
    String title,

    @Schema(description = "Descripción del activo", example = "Video mostrando la nueva colección de verano")
    String description,

    @Schema(description = "Ancho en píxeles (para imágenes y videos)", example = "1920")
    Integer width,

    @Schema(description = "Alto en píxeles (para imágenes y videos)", example = "1080")
    Integer height,

    @Schema(description = "Duración en segundos (para videos)", example = "120")
    Integer duration,

    @Schema(description = "ID del usuario que subió el archivo", example = "1")
    Long uploadedBy,

    @Schema(description = "Fecha de creación", example = "2024-05-31T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Fecha de última actualización", example = "2024-05-31T12:00:00")
    LocalDateTime updatedAt
) {} 