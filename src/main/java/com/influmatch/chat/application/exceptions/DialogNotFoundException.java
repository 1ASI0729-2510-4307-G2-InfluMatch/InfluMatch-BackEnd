package com.influmatch.chat.application.exceptions;

public class DialogNotFoundException extends RuntimeException {
    public DialogNotFoundException() {
        super("dialog_not_found");
    }
} 