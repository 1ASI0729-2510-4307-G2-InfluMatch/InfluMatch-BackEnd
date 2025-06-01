package com.influmatch.ratings.domain.repository;

import com.influmatch.ratings.domain.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    Page<Rating> findByWriterId(Long writerId, Pageable pageable);
    
    Page<Rating> findByTargetId(Long targetId, Pageable pageable);
    
    Page<Rating> findByCampaignId(Long campaignId, Pageable pageable);
    
    @Query("SELECT r FROM Rating r WHERE " +
           "(:writerId IS NULL OR r.writerId = :writerId) AND " +
           "(:targetId IS NULL OR r.targetId = :targetId) AND " +
           "(:campaignId IS NULL OR r.campaign.id = :campaignId)")
    Page<Rating> findByFilters(Long writerId, Long targetId, Long campaignId, Pageable pageable);
}
