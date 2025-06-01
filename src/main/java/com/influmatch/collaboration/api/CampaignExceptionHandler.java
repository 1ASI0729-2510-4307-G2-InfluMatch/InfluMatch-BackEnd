package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.exceptions.CampaignNotFoundException;
import com.influmatch.collaboration.application.exceptions.InvalidCampaignStateException;
import com.influmatch.collaboration.application.exceptions.NotAuthorizedCampaignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CampaignExceptionHandler {

    @ExceptionHandler(CampaignNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCampaignNotFound(CampaignNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), "La campaña no existe"));
    }

    @ExceptionHandler(NotAuthorizedCampaignException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(NotAuthorizedCampaignException e) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(e.getMessage(), "No tiene permiso para realizar esta acción"));
    }

    @ExceptionHandler(InvalidCampaignStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidCampaignStateException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "La campaña no puede ser modificada en su estado actual"));
    }
} 