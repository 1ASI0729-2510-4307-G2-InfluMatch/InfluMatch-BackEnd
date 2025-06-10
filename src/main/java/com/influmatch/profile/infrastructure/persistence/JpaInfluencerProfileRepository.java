package com.influmatch.profile.infrastructure.persistence;

import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.domain.repository.InfluencerProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaInfluencerProfileRepository extends JpaRepository<InfluencerProfile, Long>, InfluencerProfileRepository {
    @Override
    Optional<InfluencerProfile> findByUserId(Long userId);

    @Override
    boolean existsByUserId(Long userId);
} 