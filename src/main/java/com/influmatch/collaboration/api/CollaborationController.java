package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.CollaborationService;
import com.influmatch.collaboration.domain.model.CollaborationRequest;
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

@Tag(name = "Collaboration", description = "API para gestión de solicitudes de colaboración")
@RestController
@RequestMapping("/api/collaboration/requests")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CollaborationController {

    private final CollaborationService collaborationService;

    @Operation(
        summary = "Listar solicitudes de colaboración",
        description = "Recupera todas las solicitudes de colaboración del usuario autenticado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de solicitudes recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CollaborationRequestResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<CollaborationRequestResponse>> getRequests(
            @Parameter(description = "true para ver solicitudes recibidas, false para enviadas", example = "true")
            @RequestParam(defaultValue = "true") boolean received,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        List<CollaborationRequest> requests = collaborationService.getRequestsByUser(userId, received);
        
        return ResponseEntity.ok(
            requests.stream()
                .map(this::toResponse)
                .toList()
        );
    }

    @Operation(
        summary = "Obtener solicitud de colaboración",
        description = "Recupera los detalles de una solicitud específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Solicitud encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CollaborationRequestResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Solicitud no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"collaboration_request_not_found\",\"message\":\"La solicitud no existe\"}"
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
    public ResponseEntity<CollaborationRequestResponse> getRequest(
            @Parameter(description = "ID de la solicitud", example = "1")
            @PathVariable Long id) {
        
        CollaborationRequest request = collaborationService.getRequestById(id);
        return ResponseEntity.ok(toResponse(request));
    }

    @Operation(
        summary = "Crear solicitud de colaboración",
        description = "Envía una nueva solicitud de colaboración a otro usuario"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Solicitud creada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CollaborationRequestResponse.class)
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
                        name = "Usuario requerido",
                        value = "{\"error\":\"to_user_required\",\"message\":\"Debe especificar el usuario destinatario\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Mensaje muy largo",
                        value = "{\"error\":\"message_too_long\",\"message\":\"El mensaje excede el límite de caracteres\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Solicitud duplicada",
                        value = "{\"error\":\"duplicate_collaboration_request\",\"message\":\"Ya existe una solicitud pendiente para este usuario\"}"
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
    public ResponseEntity<CollaborationRequestResponse> createRequest(
            @Valid @RequestBody CreateCollaborationRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        CollaborationRequest collab = collaborationService.createRequest(request, userId);
        
        return ResponseEntity.ok(toResponse(collab));
    }

    @Operation(
        summary = "Actualizar estado de solicitud",
        description = "Cambia el estado de una solicitud (aceptar, rechazar o cancelar)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estado actualizado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CollaborationRequestResponse.class)
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
                        name = "Estado requerido",
                        value = "{\"error\":\"status_required\",\"message\":\"Debe especificar el nuevo estado\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Estado inválido",
                        value = "{\"error\":\"invalid_status\",\"message\":\"El estado especificado no es válido\"}"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para esta acción",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"not_authorized_collaboration\",\"message\":\"No tiene permiso para realizar esta acción\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Solicitud no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"collaboration_request_not_found\",\"message\":\"La solicitud no existe\"}"
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
    public ResponseEntity<CollaborationRequestResponse> updateRequest(
            @Parameter(description = "ID de la solicitud", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateCollaborationRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        CollaborationRequest collab = collaborationService.updateRequestStatus(id, request, userId);
        
        return ResponseEntity.ok(toResponse(collab));
    }

    @Operation(
        summary = "Eliminar solicitud",
        description = "Elimina una solicitud de colaboración (solo el remitente puede hacerlo)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Solicitud eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para esta acción",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"not_authorized_collaboration\",\"message\":\"No tiene permiso para realizar esta acción\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Solicitud no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"collaboration_request_not_found\",\"message\":\"La solicitud no existe\"}"
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
    public ResponseEntity<Void> deleteRequest(
            @Parameter(description = "ID de la solicitud", example = "1")
            @PathVariable Long id,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        collaborationService.deleteRequest(id, userId);
        
        return ResponseEntity.noContent().build();
    }

    private CollaborationRequestResponse toResponse(CollaborationRequest request) {
        return new CollaborationRequestResponse(
            request.getId(),
            request.getFromUserId(),
            request.getToUserId(),
            request.getStatus(),
            request.getMessage(),
            request.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            request.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
} 