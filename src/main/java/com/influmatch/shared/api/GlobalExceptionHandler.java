package com.influmatch.shared.api;

import com.influmatch.identityaccess.application.exceptions.EmailInUseException;
import com.influmatch.identityaccess.application.exceptions.RateLimitException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce excepciones a respuestas JSON homogéneas.
 *
 * • 400 – errores de validación Bean-Validation  
 * • 409 – email duplicado  
 * • 401 – credenciales incorrectas / email inexistente
 * • 429 - too many requests (rate limit)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ───────── 400 ─ Bean-Validation ───────── */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {

        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(
                Map.of("error", "validation_failed", "fields", fields));
    }

    /* ───────── 409 ─ email duplicado ───────── */
    @ExceptionHandler(EmailInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> duplicatedEmail(EmailInUseException ex) {
        return Map.of("error", ex.getMessage());          // «email_in_use»
    }

    /* ───────── 429 ─ rate limit exceeded ───────── */
    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Map<String, String> rateLimitExceeded(RateLimitException ex) {
        return Map.of(
            "error", "rate_limit_exceeded",
            "message", ex.getMessage(),
            "retry_after", "3600"  // 1 hour in seconds
        );
    }

    /* ───────── 401 ─ email inexistente ───────── */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> emailNotFound() {
        return Map.of("error", "email_not_found");
    }

    /* ───────── 401 ─ contraseña incorrecta ───────── */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> badPassword(IllegalArgumentException ex) {
        // se lanza con mensaje «bad_credentials» desde AuthService.login()
        if (!"bad_credentials".equals(ex.getMessage())) throw ex;
        return Map.of("error", "invalid_password");
    }
}
