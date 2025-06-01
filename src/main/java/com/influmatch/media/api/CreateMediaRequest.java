package com.influmatch.media.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Datos para crear un nuevo activo multimedia")
public record CreateMediaRequest(
    @Schema(description = "Archivo multimedia a subir", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "file_required")
    MultipartFile file,

    @Schema(description = "Título del activo", example = "Video promocional verano 2024")
    String title,

    @Schema(description = "Descripción del activo", example = "Video mostrando la nueva colección de verano")
    @Size(max = 1000, message = "description_too_long")
    String description
) {} 