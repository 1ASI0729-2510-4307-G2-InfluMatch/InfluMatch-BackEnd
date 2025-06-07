package com.influmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Bio {
    String value;

    private Bio(String value) {
        if (value != null && value.length() > 255) {
            throw new IllegalArgumentException("bio_length");
        }
        this.value = value;
    }

    public static Bio of(String value) {
        return new Bio(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 