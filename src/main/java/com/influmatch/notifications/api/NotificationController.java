package com.influmatch.notifications.api;

import com.influmatch.notifications.application.NotificationService;
import com.influmatch.notifications.domain.model.Notification;
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

@Tag(name = "Notifications", description = "API para gestión de notificaciones")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
        summary = "Listar notificaciones",
        description = "Recupera todas las notificaciones del usuario autenticado con soporte para paginación"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de notificaciones recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = NotificationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @Parameter(description = "Filtrar solo notificaciones no leídas", example = "true")
            @RequestParam(required = false, defaultValue = "false") boolean unread,
            @ParameterObject Pageable pageable,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Page<Notification> notifications = notificationService.getUserNotifications(userId, unread, pageable);
        
        return ResponseEntity.ok(notifications.map(this::toResponse));
    }

    @Operation(
        summary = "Obtener notificación",
        description = "Recupera los detalles de una notificación específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Notificación recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = NotificationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para ver esta notificación",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_notification\",\"message\":\"No tiene permiso para ver esta notificación\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notificación no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"notification_not_found\",\"message\":\"La notificación no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotification(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long notificationId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Notification notification = notificationService.getNotification(notificationId, userId);
        return ResponseEntity.ok(toResponse(notification));
    }

    @Operation(
        summary = "Crear notificación",
        description = "Crea una nueva notificación en el sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Notificación creada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = NotificationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"invalid_user\",\"message\":\"El ID del usuario es inválido\"}"
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
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        
        Notification notification = notificationService.createNotification(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toResponse(notification));
    }

    @Operation(
        summary = "Marcar como leída",
        description = "Marca una notificación como leída"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Notificación marcada como leída exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = NotificationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para modificar esta notificación",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_notification\",\"message\":\"No tiene permiso para modificar esta notificación\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notificación no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"notification_not_found\",\"message\":\"La notificación no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long notificationId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        Notification notification = notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(toResponse(notification));
    }

    @Operation(
        summary = "Eliminar notificación",
        description = "Elimina una notificación existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Notificación eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para eliminar esta notificación",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"unauthorized_notification\",\"message\":\"No tiene permiso para eliminar esta notificación\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notificación no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"error\":\"notification_not_found\",\"message\":\"La notificación no existe\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content
        )
    })
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long notificationId,
            Authentication auth) {
        
        Long userId = Long.parseLong(auth.getName());
        notificationService.deleteNotification(notificationId, userId);
        return ResponseEntity.noContent().build();
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getUserId(),
            notification.getType(),
            notification.getPayload(),
            notification.getCreatedAt(),
            notification.getReadAt()
        );
    }
} 