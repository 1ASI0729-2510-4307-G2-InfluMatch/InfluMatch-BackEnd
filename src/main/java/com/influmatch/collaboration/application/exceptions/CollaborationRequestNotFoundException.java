package com.influmatch.collaboration.application.exceptions;

public class CollaborationRequestNotFoundException extends RuntimeException {
    public CollaborationRequestNotFoundException() {
        super("collaboration_request_not_found");
    }
} 