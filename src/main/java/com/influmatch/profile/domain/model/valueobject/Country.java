package com.influmatch.profile.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Locale;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {
    private String value;

    public Country(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Country code cannot be empty");
        }

        code = code.toUpperCase();
        if (!isValidCountryCode(code)) {
            throw new IllegalArgumentException("Invalid country code: " + code);
        }

        this.value = code;
    }

    private boolean isValidCountryCode(String code) {
        return Arrays.stream(Locale.getISOCountries())
                .anyMatch(isoCode -> isoCode.equals(code));
    }

    @Override
    public String toString() {
        return value;
    }
} 