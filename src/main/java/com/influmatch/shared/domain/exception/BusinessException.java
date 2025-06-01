package com.influmatch.shared.domain.exception;

public class BusinessException extends RuntimeException {
    private final String error;
    private final String message;

    public BusinessException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 