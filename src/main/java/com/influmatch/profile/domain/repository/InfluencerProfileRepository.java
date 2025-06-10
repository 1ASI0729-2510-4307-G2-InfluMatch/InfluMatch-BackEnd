package com.influmatch.profile.domain.repository;

import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfluencerProfileRepository extends JpaRepository<InfluencerProfile, Long> {
    Optional<InfluencerProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
} 