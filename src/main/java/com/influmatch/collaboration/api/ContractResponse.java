package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Detalles de un contrato")
public record ContractResponse(
    @Schema(description = "ID único del contrato", example = "1")
    Long id,

    @Schema(description = "ID de la campaña asociada", example = "123")
    Long campaignId,

    @Schema(description = "URL al documento con los términos", example = "https://storage.influmatch.com/contracts/123.pdf")
    String termsUrl,

    @Schema(description = "Fecha de firma de la marca", example = "2024-05-31T12:00:00")
    LocalDateTime signedBrandAt,

    @Schema(description = "Fecha de firma del influencer", example = "2024-05-31T12:00:00")
    LocalDateTime signedInflAt,

    @Schema(description = "Fecha de creación", example = "2024-05-31T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "Fecha de última actualización", example = "2024-05-31T12:00:00")
    LocalDateTime updatedAt
) {} 