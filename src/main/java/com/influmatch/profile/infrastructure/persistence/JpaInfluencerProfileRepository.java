package com.influmatch.profile.infrastructure.persistence;

import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.domain.repository.InfluencerProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaInfluencerProfileRepository extends InfluencerProfileRepository {
    @Override
    Optional<InfluencerProfile> findByUserId(Long userId);

    @Override
    boolean existsByUserId(Long userId);
} 