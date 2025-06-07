package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.CampaignService;
import com.influmatch.collaboration.domain.assembler.CampaignAssembler;
import com.influmatch.collaboration.domain.model.Campaign;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
@Tag(name = "Campañas", description = "API para gestionar campañas")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @Operation(
        summary = "Listar campañas",
        description = "Obtiene todas las campañas disponibles"
    )
    @GetMapping
    public ResponseEntity<List<CampaignResponse>> getAllCampaigns() {
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(
            campaigns.stream()
                .map(CampaignAssembler::toResponse)
                .toList()
        );
    }

    @Operation(
        summary = "Obtener campaña",
        description = "Obtiene una campaña por su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Campaña encontrada"
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
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getCampaignById(
        @Parameter(description = "ID de la campaña") @PathVariable Long id
    ) {
        Campaign campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(CampaignAssembler.toResponse(campaign));
    }

    @Operation(
        summary = "Crear campaña",
        description = "Crea una nueva campaña"
    )
    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(
        @Valid @RequestBody CreateCampaignRequest request,
        Authentication auth
    ) {
        Long brandId = Long.parseLong(auth.getName());
        Campaign campaign = campaignService.createCampaign(request, brandId);
        return ResponseEntity.ok(CampaignAssembler.toResponse(campaign));
    }

    @Operation(
        summary = "Actualizar campaña",
        description = "Actualiza una campaña existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateCampaign(
        @Parameter(description = "ID de la campaña") @PathVariable Long id,
        @Valid @RequestBody UpdateCampaignRequest request,
        Authentication auth
    ) {
        Long brandId = Long.parseLong(auth.getName());
        Campaign campaign = campaignService.updateCampaign(id, request, brandId);
        return ResponseEntity.ok(CampaignAssembler.toResponse(campaign));
    }

    @Operation(
        summary = "Eliminar campaña",
        description = "Elimina una campaña existente"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(
        @Parameter(description = "ID de la campaña") @PathVariable Long id,
        Authentication auth
    ) {
        Long brandId = Long.parseLong(auth.getName());
        campaignService.deleteCampaign(id, brandId);
        return ResponseEntity.noContent().build();
    }
} 