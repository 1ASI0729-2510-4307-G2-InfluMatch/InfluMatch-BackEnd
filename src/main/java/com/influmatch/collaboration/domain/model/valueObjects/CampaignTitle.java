package com.influmatch.collaboration.domain.model.valueObjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CampaignTitle(
    @NotBlank(message = "title_required")
    @Size(max = 100, message = "title_too_long")
    String value
) {
    public static CampaignTitle of(String value) {
        return new CampaignTitle(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 