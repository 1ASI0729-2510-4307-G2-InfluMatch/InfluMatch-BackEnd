package com.influmatch.collaboration.domain.exception;

public class CollaborationException extends RuntimeException {
    public CollaborationException(String message) {
        super(message);
    }

    public CollaborationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class InvalidStateException extends CollaborationException {
        public InvalidStateException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedActionException extends CollaborationException {
        public UnauthorizedActionException(String message) {
            super(message);
        }
    }

    public static class CollaborationNotFoundException extends CollaborationException {
        public CollaborationNotFoundException(String message) {
            super(message);
        }
    }
} 