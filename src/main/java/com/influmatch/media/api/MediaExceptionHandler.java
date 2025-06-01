package com.influmatch.media.api;

import com.influmatch.media.application.exceptions.MediaNotFoundException;
import com.influmatch.media.application.exceptions.NotAuthorizedMediaException;
import com.influmatch.media.application.exceptions.UnsupportedMediaTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class MediaExceptionHandler {

    @ExceptionHandler(MediaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMediaNotFound(MediaNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), "El activo multimedia no existe"));
    }

    @ExceptionHandler(NotAuthorizedMediaException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(NotAuthorizedMediaException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(e.getMessage(), "No tiene permiso para realizar esta operación"));
    }

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedType(UnsupportedMediaTypeException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "Tipo de archivo no soportado"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeExceeded(MaxUploadSizeExceededException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("file_too_large", "El archivo excede el tamaño máximo permitido"));
    }
} 