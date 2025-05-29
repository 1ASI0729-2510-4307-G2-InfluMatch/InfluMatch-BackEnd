package com.influmatch.collaboration.domain.repository;

import com.influmatch.collaboration.domain.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {}
