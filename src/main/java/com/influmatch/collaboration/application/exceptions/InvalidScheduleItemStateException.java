package com.influmatch.collaboration.application.exceptions;

public class InvalidScheduleItemStateException extends RuntimeException {
    public InvalidScheduleItemStateException() {
        super("invalid_schedule_item_state");
    }
} 