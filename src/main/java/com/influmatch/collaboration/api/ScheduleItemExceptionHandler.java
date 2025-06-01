package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.exceptions.InvalidScheduleItemStateException;
import com.influmatch.collaboration.application.exceptions.ScheduleItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScheduleItemExceptionHandler {

    @ExceptionHandler(ScheduleItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleScheduleItemNotFound(ScheduleItemNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), "El ítem no existe"));
    }

    @ExceptionHandler(InvalidScheduleItemStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidScheduleItemStateException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "No se puede eliminar un ítem completado si la campaña no está cancelada"));
    }
} 