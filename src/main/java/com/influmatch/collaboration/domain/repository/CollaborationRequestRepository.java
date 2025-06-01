package com.influmatch.collaboration.domain.repository;

import com.influmatch.collaboration.domain.model.CollaborationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest, Long> {
    
    // Buscar solicitudes enviadas por un usuario
    List<CollaborationRequest> findByFromUserId(Long userId);
    
    // Buscar solicitudes recibidas por un usuario
    List<CollaborationRequest> findByToUserId(Long userId);
    
    // Verificar si ya existe una solicitud pendiente entre dos usuarios
    boolean existsByFromUserIdAndToUserIdAndStatus(
        Long fromUserId, 
        Long toUserId, 
        com.influmatch.collaboration.domain.model.CollaborationStatus status
    );
} 