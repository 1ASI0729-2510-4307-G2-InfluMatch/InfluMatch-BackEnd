package com.influmatch.chat.api;

import com.influmatch.chat.application.DialogService;
import com.influmatch.chat.domain.model.Dialog;
import com.influmatch.chat.domain.model.Message;
import com.influmatch.shared.api.ErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;

@Tag(name = "Dialogs", description = "API para gestión de diálogos")
@RestController
@RequestMapping("/api/dialogs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DialogController {

    private final DialogService dialogService;

    @Operation(
        summary = "Listar diálogos",
        description = "Recupera todos los diálogos del usuario autenticado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de diálogos recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = DialogResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<DialogResponse>> getDialogs(Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        List<Dialog> dialogs = dialogService.getUserDialogs(userId);
        
        return ResponseEntity.ok(
            dialogs.stream()
                .map(dialog -> toResponse(dialog, List.of()))  // Sin mensajes en el listado
                .toList()
        );
    }

    @Operation(
        summary = "Obtener diálogo",
        description = "Recupera un diálogo específico con sus últimos mensajes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Diálogo recuperado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = DialogResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_dialog\",\"message\":\"No tiene permiso para acceder a este diálogo\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Diálogo no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"dialog_not_found\",\"message\":\"El diálogo no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping("/{dialogId}")
    public ResponseEntity<DialogResponse> getDialog(
            @Parameter(description = "ID del diálogo", example = "1")
            @PathVariable Long dialogId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Dialog dialog = dialogService.getDialog(dialogId, userId);
        List<Message> messages = dialogService.getRecentMessages(dialogId, userId);
        
        return ResponseEntity.ok(toResponse(dialog, messages));
    }

    @Operation(
        summary = "Crear diálogo",
        description = "Crea un nuevo diálogo entre participantes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Diálogo creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = DialogResponse.class)
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
                        name = "Participantes requeridos",
                        value = "{\"error\":\"participants_required\",\"message\":\"Debe especificar los participantes\"}"
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "Mínimo participantes",
                        value = "{\"error\":\"min_two_participants\",\"message\":\"Se requieren al menos dos participantes\"}"
                    )
                }
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
    public ResponseEntity<DialogResponse> createDialog(
            @Valid @RequestBody CreateDialogRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Dialog dialog = dialogService.createDialog(request, userId);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toResponse(dialog, List.of()));
    }

    @Operation(
        summary = "Eliminar diálogo",
        description = "Elimina un diálogo y todos sus mensajes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Diálogo eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"invalid_dialog_state\",\"message\":\"No se puede eliminar un diálogo con mensajes no leídos\"}"
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
                    value = "{\"error\":\"unauthorized_dialog\",\"message\":\"No tiene permiso para eliminar este diálogo\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Diálogo no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"dialog_not_found\",\"message\":\"El diálogo no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @DeleteMapping("/{dialogId}")
    public ResponseEntity<Void> deleteDialog(
            @Parameter(description = "ID del diálogo", example = "1")
            @PathVariable Long dialogId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        dialogService.deleteDialog(dialogId, userId);
        
        return ResponseEntity.noContent().build();
    }

    private DialogResponse toResponse(Dialog dialog, List<Message> messages) {
        return new DialogResponse(
            dialog.getId(),
            dialog.getCampaign() != null ? dialog.getCampaign().getId() : null,
            dialog.getParticipantIds(),
            messages.stream()
                .map(msg -> new MessageResponse(
                    msg.getId(),
                    msg.getDialog().getId(),
                    msg.getSenderId(),
                    msg.getContent(),
                    msg.getAssetId(),
                    msg.getCreatedAt(),
                    msg.getReadAt()
                ))
                .toList(),
            dialog.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            dialog.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
} 