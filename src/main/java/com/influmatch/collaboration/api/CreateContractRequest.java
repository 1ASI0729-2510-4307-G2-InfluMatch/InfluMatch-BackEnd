package com.influmatch.collaboration.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para crear un nuevo contrato")
public record CreateContractRequest(
    @Schema(description = "URL al documento con los términos del contrato", example = "https://storage.influmatch.com/contracts/123.pdf")
    @NotBlank(message = "terms_url_required")
    String termsUrl
) {} 