package com.influmatch.collaboration.domain.repository;

import com.influmatch.collaboration.domain.model.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {
    
    // Buscar ítems por campaña
    List<ScheduleItem> findByCampaignId(Long campaignId);
    
    // Verificar si un ítem pertenece a una campaña
    boolean existsByIdAndCampaignId(Long id, Long campaignId);
} 