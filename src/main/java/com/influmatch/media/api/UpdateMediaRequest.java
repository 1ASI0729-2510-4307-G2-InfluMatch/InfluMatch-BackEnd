package com.influmatch.media.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos actualizables de un activo multimedia")
public record UpdateMediaRequest(
    @Schema(description = "Nuevo título del activo", example = "Video promocional verano 2024")
    String title,

    @Schema(description = "Nueva descripción del activo", example = "Video mostrando la nueva colección de verano")
    @Size(max = 1000, message = "description_too_long")
    String description
) {} 