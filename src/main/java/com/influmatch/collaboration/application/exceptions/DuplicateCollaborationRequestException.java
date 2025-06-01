package com.influmatch.collaboration.application.exceptions;

public class DuplicateCollaborationRequestException extends RuntimeException {
    public DuplicateCollaborationRequestException() {
        super("duplicate_collaboration_request");
    }
} 