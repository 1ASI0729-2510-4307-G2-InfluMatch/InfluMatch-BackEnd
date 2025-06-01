package com.influmatch.chat.application.exceptions;

public class InvalidDialogStateException extends RuntimeException {
    public InvalidDialogStateException() {
        super("invalid_dialog_state");
    }
} 