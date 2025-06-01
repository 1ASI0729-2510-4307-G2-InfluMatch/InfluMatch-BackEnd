package com.influmatch.chat.api;

import com.influmatch.chat.application.MessageService;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Messages", description = "API para gestión de mensajes en diálogos")
@RestController
@RequestMapping("/api/dialogs/{dialogId}/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;

    @Operation(
        summary = "Listar mensajes",
        description = "Recupera todos los mensajes de un diálogo, con soporte para paginación"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de mensajes recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MessageResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para ver los mensajes",
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
    @GetMapping
    public ResponseEntity<Page<MessageResponse>> getMessages(
            @Parameter(description = "ID del diálogo", example = "1")
            @PathVariable Long dialogId,
            @ParameterObject Pageable pageable,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Page<Message> messages = messageService.getDialogMessages(dialogId, userId, pageable);
        
        return ResponseEntity.ok(messages.map(this::toResponse));
    }

    @Operation(
        summary = "Crear mensaje",
        description = "Envía un nuevo mensaje en el diálogo especificado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Mensaje creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MessageResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"validation_error\",\"message\":\"El contenido del mensaje es requerido\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para enviar mensajes",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_dialog\",\"message\":\"No tiene permiso para enviar mensajes en este diálogo\"}"
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
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(
            @Parameter(description = "ID del diálogo", example = "1")
            @PathVariable Long dialogId,
            @Valid @RequestBody CreateMessageRequest request,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Message message = messageService.createMessage(dialogId, userId, request);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toResponse(message));
    }

    @Operation(
        summary = "Marcar mensaje como leído",
        description = "Actualiza la fecha de lectura del mensaje especificado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Mensaje marcado como leído exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MessageResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para marcar el mensaje",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_message\",\"message\":\"No puede marcar como leído un mensaje propio\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Mensaje no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"message_not_found\",\"message\":\"El mensaje no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PutMapping("/{messageId}/read")
    public ResponseEntity<MessageResponse> markAsRead(
            @Parameter(description = "ID del diálogo", example = "1")
            @PathVariable Long dialogId,
            @Parameter(description = "ID del mensaje", example = "1")
            @PathVariable Long messageId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Message message = messageService.markAsRead(dialogId, messageId, userId);
        
        return ResponseEntity.ok(toResponse(message));
    }

    @Operation(
        summary = "Eliminar mensaje",
        description = "Elimina un mensaje no leído del diálogo"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Mensaje eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"invalid_message_state\",\"message\":\"No se puede eliminar un mensaje que ya ha sido leído\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para eliminar el mensaje",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_message\",\"message\":\"Solo el autor puede eliminar el mensaje\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Mensaje no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"message_not_found\",\"message\":\"El mensaje no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "ID del diálogo", example = "1")
            @PathVariable Long dialogId,
            @Parameter(description = "ID del mensaje", example = "1")
            @PathVariable Long messageId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        messageService.deleteMessage(dialogId, messageId, userId);
        
        return ResponseEntity.noContent().build();
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
            message.getId(),
            message.getDialog().getId(),
            message.getSenderId(),
            message.getContent(),
            message.getAssetId(),
            message.getCreatedAt(),
            message.getReadAt()
        );
    }
} 