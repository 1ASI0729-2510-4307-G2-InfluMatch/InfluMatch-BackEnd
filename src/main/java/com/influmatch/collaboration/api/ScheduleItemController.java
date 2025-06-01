package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.ScheduleItemService;
import com.influmatch.collaboration.domain.model.ScheduleItem;
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

@Tag(name = "Schedule Items", description = "API para gestión de ítems de cronograma")
@RestController
@RequestMapping("/api/campaigns/{campaignId}/schedule")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ScheduleItemController {

    private final ScheduleItemService scheduleItemService;

    @Operation(
        summary = "Listar ítems de cronograma",
        description = "Recupera todos los ítems de cronograma de una campaña"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de ítems recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ScheduleItemResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<ScheduleItemResponse>> getScheduleItems(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long campaignId) {
        
        List<ScheduleItem> items = scheduleItemService.getScheduleItemsByCampaignId(campaignId);
        return ResponseEntity.ok(
            items.stream()
                .map(this::toResponse)
                .toList()
        );
    }

    @Operation(
        summary = "Crear ítem de cronograma",
        description = "Añade un nuevo ítem al cronograma de la campaña"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ítem creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ScheduleItemResponse.class)
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
                        name = "Título requerido",
                        value = "{\"error\":\"title_required\",\"message\":\"Debe especificar el título de la tarea\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Título muy largo",
                        value = "{\"error\":\"title_too_long\",\"message\":\"El título excede el límite de caracteres\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha requerida",
                        value = "{\"error\":\"due_date_required\",\"message\":\"Debe especificar la fecha límite\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Fecha futura",
                        value = "{\"error\":\"due_date_must_be_future\",\"message\":\"La fecha límite debe ser futura\"}"
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
    @PostMapping
    public ResponseEntity<ScheduleItemResponse> createScheduleItem(
            @Parameter(description = "ID de la campaña", example = "1")
            @PathVariable Long campaignId,
            @Valid @RequestBody CreateScheduleItemRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        ScheduleItem item = scheduleItemService.createScheduleItem(campaignId, request, userId);
        
        return ResponseEntity.ok(toResponse(item));
    }

    @Operation(
        summary = "Actualizar ítem de cronograma",
        description = "Modifica los datos de un ítem existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ítem actualizado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ScheduleItemResponse.class)
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
                        name = "Fecha futura",
                        value = "{\"error\":\"due_date_must_be_future\",\"message\":\"La fecha límite debe ser futura\"}"
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
            description = "Ítem no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"schedule_item_not_found\",\"message\":\"El ítem no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PutMapping("/{itemId}")
    public ResponseEntity<ScheduleItemResponse> updateScheduleItem(
            @Parameter(description = "ID del ítem", example = "1")
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateScheduleItemRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        ScheduleItem item = scheduleItemService.updateScheduleItem(itemId, request, userId);
        
        return ResponseEntity.ok(toResponse(item));
    }

    @Operation(
        summary = "Eliminar ítem de cronograma",
        description = "Elimina un ítem del cronograma"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Ítem eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"invalid_schedule_item_state\",\"message\":\"No se puede eliminar un ítem completado si la campaña no está cancelada\"}"
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
                    value = "{\"error\":\"not_authorized_campaign\",\"message\":\"No tiene permiso para modificar esta campaña\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ítem no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"schedule_item_not_found\",\"message\":\"El ítem no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteScheduleItem(
            @Parameter(description = "ID del ítem", example = "1")
            @PathVariable Long itemId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        scheduleItemService.deleteScheduleItem(itemId, userId);
        
        return ResponseEntity.noContent().build();
    }

    private ScheduleItemResponse toResponse(ScheduleItem item) {
        return new ScheduleItemResponse(
            item.getId(),
            item.getCampaign().getId(),
            item.getTitle(),
            item.getDueDate(),
            item.getDoneAt(),
            item.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            item.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
} 