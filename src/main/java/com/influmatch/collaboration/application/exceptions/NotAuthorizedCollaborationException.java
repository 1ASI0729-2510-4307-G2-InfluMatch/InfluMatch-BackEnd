package com.influmatch.collaboration.application.exceptions;

public class NotAuthorizedCollaborationException extends RuntimeException {
    public NotAuthorizedCollaborationException() {
        super("not_authorized_collaboration");
    }
} 