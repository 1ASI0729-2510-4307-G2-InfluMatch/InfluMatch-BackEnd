package com.influmatch.chat.domain.model.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Long chatId) {
        super("Chat not found with id: " + chatId);
    }
} 