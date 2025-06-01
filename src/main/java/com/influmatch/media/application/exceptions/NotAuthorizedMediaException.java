package com.influmatch.media.application.exceptions;

public class NotAuthorizedMediaException extends RuntimeException {
    public NotAuthorizedMediaException() {
        super("not_authorized");
    }
} 