package com.influmatch.profile.domain.repository;

import com.influmatch.profile.domain.model.entity.InfluencerProfile;

import java.util.Optional;

public interface InfluencerProfileRepository {
    InfluencerProfile save(InfluencerProfile profile);
    Optional<InfluencerProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
} 