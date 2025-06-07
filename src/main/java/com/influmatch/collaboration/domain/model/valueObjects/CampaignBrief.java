package com.influmatch.collaboration.domain.model.valueObjects;

import jakarta.validation.constraints.Size;

public record CampaignBrief(
    @Size(max = 2000, message = "brief_too_long")
    String value
) {
    public static CampaignBrief of(String value) {
        return new CampaignBrief(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 