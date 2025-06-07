package com.influmatch.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class FollowersCount {
    Long value;

    private FollowersCount(Long value) {
        if (value != null && value < 0) {
            throw new IllegalArgumentException("followers_count_negative");
        }
        this.value = value;
    }

    public static FollowersCount of(Long value) {
        return new FollowersCount(value);
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "0";
    }
} 