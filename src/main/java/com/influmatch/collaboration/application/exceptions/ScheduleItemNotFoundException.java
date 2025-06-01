package com.influmatch.collaboration.application.exceptions;

public class ScheduleItemNotFoundException extends RuntimeException {
    public ScheduleItemNotFoundException() {
        super("schedule_item_not_found");
    }
} 