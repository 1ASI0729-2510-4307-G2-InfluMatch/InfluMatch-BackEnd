package com.influmatch.collaboration.domain.repository;

import com.influmatch.collaboration.domain.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    
    // Buscar campañas por marca
    List<Campaign> findByBrandId(Long brandId);
    
    // Buscar campañas por influencer
    List<Campaign> findByInfluencerId(Long influencerId);
    
    // Verificar si una marca es propietaria de una campaña
    boolean existsByIdAndBrandId(Long id, Long brandId);
}
