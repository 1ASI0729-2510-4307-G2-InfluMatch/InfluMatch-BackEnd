package com.influmatch.collaboration.application.exceptions;

public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException() {
        super("campaign_not_found");
    }
} 