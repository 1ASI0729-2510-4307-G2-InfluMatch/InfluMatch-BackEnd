package com.influmatch.profile.domain.repository;

import com.influmatch.profile.domain.model.entity.BrandProfile;

import java.util.Optional;

public interface BrandProfileRepository {
    BrandProfile save(BrandProfile profile);
    Optional<BrandProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
} 