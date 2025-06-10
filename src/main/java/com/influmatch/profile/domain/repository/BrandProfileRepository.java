package com.influmatch.profile.domain.repository;

import com.influmatch.profile.domain.model.entity.BrandProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandProfileRepository extends JpaRepository<BrandProfile, Long> {
    Optional<BrandProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
} 