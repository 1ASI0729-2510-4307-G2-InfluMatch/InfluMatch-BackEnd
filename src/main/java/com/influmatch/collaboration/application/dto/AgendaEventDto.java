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
@Schema(description = "Evento de agenda de una colaboración")
public class AgendaEventDto {
    @Schema(description = "ID de la colaboración")
    private Long collaborationId;

    @Schema(description = "ID del usuario que envió la solicitud")
    private Long initiatorId;

    @Schema(description = "ID del usuario que recibió la solicitud")
    private Long counterpartId;

    @Schema(description = "Fecha del evento", example = "2024-03-25")
    private String date;

    @Schema(description = "Título del evento", example = "Propuesta creativa")
    private String eventTitle;

    @Schema(description = "Descripción del evento", example = "Presentación de ideas y concepto")
    private String description;

    @Schema(description = "Ubicación del evento", example = "Zoom")
    private String location;

    @Schema(description = "Nombre de la contraparte")
    private String counterpartName;
} 