package com.influmatch.chat.api;

import com.influmatch.chat.application.exceptions.DialogNotFoundException;
import com.influmatch.chat.application.exceptions.InvalidDialogStateException;
import com.influmatch.chat.application.exceptions.UnauthorizedDialogException;
import com.influmatch.shared.api.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DialogExceptionHandler {

    @ExceptionHandler(DialogNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDialogNotFound(DialogNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), "El diálogo no existe"));
    }

    @ExceptionHandler(UnauthorizedDialogException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedDialogException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(e.getMessage(), "No tiene permiso para acceder a este diálogo"));
    }

    @ExceptionHandler(InvalidDialogStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidDialogStateException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "No se puede eliminar un diálogo con mensajes no leídos"));
    }
} 