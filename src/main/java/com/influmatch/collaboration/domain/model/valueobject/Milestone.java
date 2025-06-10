package com.influmatch.collaboration.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Milestone {
    private String title;
    private LocalDate date;
    private String description;
    private String location;
    private String deliverables;

    public Milestone(String title, LocalDate date, String description, String location, String deliverables) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        this.title = title.trim();
        this.date = date;
        this.description = description != null ? description.trim() : null;
        this.location = location != null ? location.trim() : null;
        this.deliverables = deliverables != null ? deliverables.trim() : null;
    }
} 