package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.exceptions.CollaborationRequestNotFoundException;
import com.influmatch.collaboration.application.exceptions.DuplicateCollaborationRequestException;
import com.influmatch.collaboration.application.exceptions.NotAuthorizedCollaborationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CollaborationExceptionHandler {

    @ExceptionHandler(CollaborationRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound(CollaborationRequestNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), "La solicitud no existe"));
    }

    @ExceptionHandler(NotAuthorizedCollaborationException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(NotAuthorizedCollaborationException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(e.getMessage(), "No tiene permiso para realizar esta acción"));
    }

    @ExceptionHandler(DuplicateCollaborationRequestException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRequest(DuplicateCollaborationRequestException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "Ya existe una solicitud pendiente para este usuario"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "El valor proporcionado no es válido"));
    }
} 