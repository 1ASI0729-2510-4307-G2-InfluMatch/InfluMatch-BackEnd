package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.ContractService;
import com.influmatch.collaboration.domain.model.Contract;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;

@Tag(name = "Contracts", description = "API para gestión de contratos")
@RestController
@RequestMapping("/api/campaigns/{campaignId}/contract")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ContractController {

    private final ContractService contractService;

    @Operation(
        summary = "Obtener contrato",
        description = "Recupera el contrato asociado a una campaña"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Contrato encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ContractResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contrato no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"contract_not_found\",\"message\":\"El contrato no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<ContractResponse> getContract(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long campaignId) {
        
        Contract contract = contractService.getContractByCampaignId(campaignId);
        return ResponseEntity.ok(toResponse(contract));
    }

    @Operation(
        summary = "Crear contrato",
        description = "Crea un nuevo contrato para la campaña"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Contrato creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ContractResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "URL requerida",
                        value = "{\"error\":\"terms_url_required\",\"message\":\"Debe especificar la URL del documento\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Contrato existente",
                        value = "{\"error\":\"contract_already_exists\",\"message\":\"Ya existe un contrato para esta campaña\"}"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"not_authorized_campaign\",\"message\":\"No tiene permiso para crear contratos en esta campaña\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Campaña no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"campaign_not_found\",\"message\":\"La campaña no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<ContractResponse> createContract(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long campaignId,
            @Valid @RequestBody CreateContractRequest request,
            Authentication auth) {
        
        Long brandId = Long.parseLong(auth.getName());
        Contract contract = contractService.createContract(campaignId, request, brandId);
        
        return ResponseEntity.ok(toResponse(contract));
    }

    @Operation(
        summary = "Firmar contrato",
        description = "Registra la firma del usuario en el contrato"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Contrato firmado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ContractResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"not_authorized_campaign\",\"message\":\"No tiene permiso para firmar este contrato\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contrato no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"contract_not_found\",\"message\":\"El contrato no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PutMapping("/sign")
    public ResponseEntity<ContractResponse> signContract(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long campaignId,
            @Parameter(description = "true si es marca, false si es influencer", example = "true")
            @RequestParam boolean isBrand,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Contract contract = contractService.signContract(campaignId, userId, isBrand);
        
        return ResponseEntity.ok(toResponse(contract));
    }

    @Operation(
        summary = "Eliminar contrato",
        description = "Elimina el contrato de una campaña"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Contrato eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"invalid_contract_state\",\"message\":\"No se puede eliminar un contrato firmado si la campaña no está cancelada\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"not_authorized_campaign\",\"message\":\"No tiene permiso para eliminar este contrato\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Contrato no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"contract_not_found\",\"message\":\"El contrato no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteContract(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long campaignId,
            Authentication auth) {
        
        Long brandId = Long.parseLong(auth.getName());
        contractService.deleteContract(campaignId, brandId);
        
        return ResponseEntity.noContent().build();
    }

    private ContractResponse toResponse(Contract contract) {
        return new ContractResponse(
            contract.getId(),
            contract.getCampaign().getId(),
            contract.getTermsUrl(),
            contract.getSignedBrandAt(),
            contract.getSignedInflAt(),
            contract.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            contract.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
} 