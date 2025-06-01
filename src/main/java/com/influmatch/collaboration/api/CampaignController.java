package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.CampaignService;
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

import java.time.ZoneId;
import java.util.List;

@Tag(name = "Campaigns", description = "API para gestión de campañas")
@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CampaignController {

    private final CampaignService campaignService;

    @Operation(
        summary = "Listar campañas",
        description = "Recupera todas las campañas registradas en el sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de campañas recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CampaignResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<CampaignResponse>> getCampaigns() {
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(
            campaigns.stream()
                .map(this::toResponse)
                .toList()
        );
    }

    @Operation(
        summary = "Obtener campaña",
        description = "Recupera los detalles de una campaña específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Campaña encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CampaignResponse.class)
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
    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getCampaign(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long id) {
        
        Campaign campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(toResponse(campaign));
    }

    @Operation(
        summary = "Crear campaña",
        description = "Crea una nueva campaña con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Campaña creada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CampaignResponse.class)
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
                        name = "Influencer requerido",
                        value = "{\"error\":\"influencer_required\",\"message\":\"Debe especificar el influencer\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Título requerido",
                        value = "{\"error\":\"title_required\",\"message\":\"Debe especificar el título\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Título muy largo",
                        value = "{\"error\":\"title_too_long\",\"message\":\"El título excede el límite de caracteres\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Brief muy largo",
                        value = "{\"error\":\"brief_too_long\",\"message\":\"El brief excede el límite de caracteres\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha inicio requerida",
                        value = "{\"error\":\"start_date_required\",\"message\":\"Debe especificar la fecha de inicio\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha fin requerida",
                        value = "{\"error\":\"end_date_required\",\"message\":\"Debe especificar la fecha de fin\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha inicio futura",
                        value = "{\"error\":\"start_date_must_be_future\",\"message\":\"La fecha de inicio debe ser futura\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha fin futura",
                        value = "{\"error\":\"end_date_must_be_future\",\"message\":\"La fecha de fin debe ser futura\"}"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(
            @Valid @RequestBody CreateCampaignRequest request,
            Authentication auth) {
        
        Long brandId = Long.parseLong(auth.getName());
        Campaign campaign = campaignService.createCampaign(request, brandId);
        
        return ResponseEntity.ok(toResponse(campaign));
    }

    @Operation(
        summary = "Actualizar campaña",
        description = "Modifica los datos de una campaña existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Campaña actualizada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CampaignResponse.class)
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
                        name = "Título muy largo",
                        value = "{\"error\":\"title_too_long\",\"message\":\"El título excede el límite de caracteres\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Brief muy largo",
                        value = "{\"error\":\"brief_too_long\",\"message\":\"El brief excede el límite de caracteres\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha inicio futura",
                        value = "{\"error\":\"start_date_must_be_future\",\"message\":\"La fecha de inicio debe ser futura\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha fin futura",
                        value = "{\"error\":\"end_date_must_be_future\",\"message\":\"La fecha de fin debe ser futura\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Estado inválido",
                        value = "{\"error\":\"invalid_campaign_state\",\"message\":\"No se puede modificar una campaña finalizada\"}"
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
                    value = "{\"error\":\"not_authorized_campaign\",\"message\":\"No tiene permiso para modificar esta campaña\"}"
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
    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateCampaign(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateCampaignRequest request,
            Authentication auth) {
        
        Long brandId = Long.parseLong(auth.getName());
        Campaign campaign = campaignService.updateCampaign(id, request, brandId);
        
        return ResponseEntity.ok(toResponse(campaign));
    }

    @Operation(
        summary = "Eliminar campaña",
        description = "Elimina una campaña existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Campaña eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"invalid_campaign_state\",\"message\":\"No se puede eliminar una campaña activa o finalizada\"}"
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
                    value = "{\"error\":\"not_authorized_campaign\",\"message\":\"No tiene permiso para eliminar esta campaña\"}"
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long id,
            Authentication auth) {
        
        Long brandId = Long.parseLong(auth.getName());
        campaignService.deleteCampaign(id, brandId);
        
        return ResponseEntity.noContent().build();
    }

    private CampaignResponse toResponse(Campaign campaign) {
        return new CampaignResponse(
            campaign.getId(),
            campaign.getBrandId(),
            campaign.getInfluencerId(),
            campaign.getTitle(),
            campaign.getBrief(),
            campaign.getStatus(),
            campaign.getStartDate(),
            campaign.getEndDate(),
            campaign.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            campaign.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
} 