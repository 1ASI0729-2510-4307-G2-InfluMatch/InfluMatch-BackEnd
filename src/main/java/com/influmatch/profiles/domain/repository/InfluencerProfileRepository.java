package com.influmatch.profiles.domain.repository;

import com.influmatch.profiles.domain.model.InfluencerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluencerProfileRepository extends JpaRepository<InfluencerProfile, Long> {
    boolean existsByUserId(Long userId);
} 