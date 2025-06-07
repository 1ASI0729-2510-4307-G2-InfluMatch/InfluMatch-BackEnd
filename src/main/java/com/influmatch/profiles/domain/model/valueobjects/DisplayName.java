package com.influmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class DisplayName {
    String value;

    private DisplayName(String value) {
        if (value != null && (value.length() < 3 || value.length() > 50)) {
            throw new IllegalArgumentException("display_name_length");
        }
        this.value = value;
    }

    public static DisplayName of(String value) {
        return new DisplayName(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 