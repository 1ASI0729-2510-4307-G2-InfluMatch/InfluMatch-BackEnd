package com.influmatch.profile.infrastructure.persistence;

import com.influmatch.profile.domain.model.entity.BrandProfile;
import com.influmatch.profile.domain.repository.BrandProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaBrandProfileRepository extends JpaRepository<BrandProfile, Long>, BrandProfileRepository {
    @Override
    Optional<BrandProfile> findByUserId(Long userId);

    @Override
    boolean existsByUserId(Long userId);
} 