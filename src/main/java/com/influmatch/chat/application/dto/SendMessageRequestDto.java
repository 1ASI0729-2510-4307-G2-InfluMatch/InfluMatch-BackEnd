package com.influmatch.chat.application.dto;

import com.influmatch.chat.domain.model.valueobject.AttachmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to send a new message")
public class SendMessageRequestDto {
    @Schema(description = "Text content of the message", example = "Hola, ¿cómo estás?")
    private String content;

    @Schema(description = "Base64 encoded file content", example = "iVBORw0KGgo...")
    private String attachmentBase64;

    @Schema(description = "Type of attachment (PHOTO, VIDEO, DOCUMENT)")
    private AttachmentType attachmentType;
} 