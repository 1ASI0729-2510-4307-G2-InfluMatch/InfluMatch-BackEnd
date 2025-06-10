package com.influmatch.profile.domain.exception;

public class ProfileException extends RuntimeException {
    public ProfileException(String message) {
        super(message);
    }

    public ProfileException(String message, Throwable cause) {
        super(message, cause);
    }
} 