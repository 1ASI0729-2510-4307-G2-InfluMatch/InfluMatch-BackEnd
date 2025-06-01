package com.influmatch.collaboration.domain.repository;

import com.influmatch.collaboration.domain.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    
    // Buscar contrato por ID de campaña
    Optional<Contract> findByCampaignId(Long campaignId);
    
    // Verificar si existe contrato para una campaña
    boolean existsByCampaignId(Long campaignId);
} 