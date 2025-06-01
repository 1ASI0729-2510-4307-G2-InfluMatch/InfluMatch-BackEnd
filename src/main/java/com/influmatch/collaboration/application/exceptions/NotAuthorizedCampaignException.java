package com.influmatch.collaboration.application.exceptions;

public class NotAuthorizedCampaignException extends RuntimeException {
    public NotAuthorizedCampaignException() {
        super("not_authorized_campaign");
    }
} 