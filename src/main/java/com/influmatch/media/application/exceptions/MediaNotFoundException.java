package com.influmatch.media.application.exceptions;

public class MediaNotFoundException extends RuntimeException {
    public MediaNotFoundException() {
        super("media_not_found");
    }
} 