package com.influmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Category {
    String value;

    private Category(String value) {
        if (value != null && value.length() > 50) {
            throw new IllegalArgumentException("category_length");
        }
        this.value = value;
    }

    public static Category of(String value) {
        return new Category(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 