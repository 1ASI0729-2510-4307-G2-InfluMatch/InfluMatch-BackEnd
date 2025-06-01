package com.influmatch.collaboration.application.exceptions;

public class InvalidCampaignStateException extends RuntimeException {
    public InvalidCampaignStateException() {
        super("invalid_campaign_state");
    }
} 