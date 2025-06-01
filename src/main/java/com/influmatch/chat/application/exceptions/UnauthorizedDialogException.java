package com.influmatch.chat.application.exceptions;

public class UnauthorizedDialogException extends RuntimeException {
    public UnauthorizedDialogException() {
        super("unauthorized_dialog");
    }
} 