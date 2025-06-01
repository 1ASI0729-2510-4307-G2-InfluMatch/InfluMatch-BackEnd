package com.influmatch.shared.domain.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(String error, String message) {
        super(error, message);
    }
} 