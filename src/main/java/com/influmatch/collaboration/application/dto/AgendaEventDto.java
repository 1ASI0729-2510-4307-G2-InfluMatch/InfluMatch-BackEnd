package com.influmatch.collaboration.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Evento en la agenda de colaboraciones")
public class AgendaEventDto {
    @Schema(description = "ID de la colaboración")
    private Long collaborationId;

    @Schema(description = "Fecha del evento", example = "2025-07-12")
    private String date;

    @Schema(description = "Título del evento", example = "Publicación de Reel")
    private String eventTitle;

    @Schema(description = "Nombre de la contraparte")
    private String counterpartName;
} 