package com.influmatch.collaboration.api;

import com.influmatch.collaboration.application.exceptions.ContractAlreadyExistsException;
import com.influmatch.collaboration.application.exceptions.ContractNotFoundException;
import com.influmatch.collaboration.application.exceptions.InvalidContractStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContractExceptionHandler {

    @ExceptionHandler(ContractNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleContractNotFound(ContractNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), "El contrato no existe"));
    }

    @ExceptionHandler(ContractAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleContractExists(ContractAlreadyExistsException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "Ya existe un contrato para esta campaña"));
    }

    @ExceptionHandler(InvalidContractStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidContractStateException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), "El contrato no puede ser modificado en su estado actual"));
    }
} 