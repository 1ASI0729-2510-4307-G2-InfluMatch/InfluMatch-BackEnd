package com.influmatch.chat.domain.repository;

import com.influmatch.chat.domain.model.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    
    // Buscar diálogos donde el usuario es participante
    @Query("SELECT d FROM Dialog d WHERE :userId MEMBER OF d.participantIds")
    List<Dialog> findByParticipantId(@Param("userId") Long userId);
    
    // Buscar diálogos por campaña
    List<Dialog> findByCampaignId(Long campaignId);
    
    // Verificar si un usuario es participante
    @Query("SELECT COUNT(d) > 0 FROM Dialog d WHERE d.id = :dialogId AND :userId MEMBER OF d.participantIds")
    boolean isParticipant(@Param("dialogId") Long dialogId, @Param("userId") Long userId);
} 