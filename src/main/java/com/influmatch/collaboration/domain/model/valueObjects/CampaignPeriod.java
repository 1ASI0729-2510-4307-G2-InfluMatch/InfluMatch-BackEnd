package com.influmatch.collaboration.domain.model.valueObjects;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CampaignPeriod(
    @NotNull(message = "start_date_required")
    @Future(message = "start_date_must_be_future")
    LocalDate startDate,

    @NotNull(message = "end_date_required")
    @Future(message = "end_date_must_be_future")
    LocalDate endDate
) {
    public CampaignPeriod {
        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("end_date_before_start_date");
        }
    }

    public static CampaignPeriod of(LocalDate startDate, LocalDate endDate) {
        return new CampaignPeriod(startDate, endDate);
    }
} 