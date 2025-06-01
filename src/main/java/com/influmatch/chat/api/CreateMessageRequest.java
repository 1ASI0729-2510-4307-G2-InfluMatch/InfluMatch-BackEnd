package com.influmatch.chat.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Solicitud para crear un nuevo mensaje")
public record CreateMessageRequest(
    @Schema(description = "Contenido del mensaje", example = "¡Hola! ¿Cómo estás?")
    @NotBlank(message = "El contenido del mensaje es requerido")
    @Size(max = 1000, message = "El contenido del mensaje no puede exceder los 1000 caracteres")
    String content,

    @Schema(description = "ID del archivo adjunto (opcional)", example = "1", nullable = true)
    Long assetId
) {} 