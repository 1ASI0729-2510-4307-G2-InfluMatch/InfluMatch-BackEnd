package com.influmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Country {
    String value;

    private Country(String value) {
        if (value != null && value.length() > 50) {
            throw new IllegalArgumentException("country_length");
        }
        this.value = value;
    }

    public static Country of(String value) {
        return new Country(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 