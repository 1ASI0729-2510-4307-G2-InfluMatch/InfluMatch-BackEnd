package com.influmatch.media.application.exceptions;

public class UnsupportedMediaTypeException extends RuntimeException {
    public UnsupportedMediaTypeException() {
        super("unsupported_media_type");
    }
} 