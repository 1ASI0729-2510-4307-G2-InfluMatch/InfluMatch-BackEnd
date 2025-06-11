package com.influmatch.collaboration.infrastructure.persistence;

import com.influmatch.collaboration.domain.model.entity.Collaboration;
import com.influmatch.collaboration.domain.model.valueobject.CollaborationStatus;
import com.influmatch.collaboration.domain.repository.CollaborationRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCollaborationRepository extends CollaborationRepository {
    @Override
    @Query("SELECT c FROM Collaboration c WHERE c.initiatorId = :userId OR c.counterpartId = :userId")
    List<Collaboration> findAllByUser(@Param("userId") Long userId);

    @Override
    @Query("SELECT c FROM Collaboration c WHERE (c.initiatorId = :userId OR c.counterpartId = :userId) AND c.status = :status")
    List<Collaboration> findAllByUserAndStatus(@Param("userId") Long userId, @Param("status") CollaborationStatus status);

    @Override
    @Query("SELECT c FROM Collaboration c WHERE (c.initiatorId = :userId OR c.counterpartId = :userId) AND c.status = 'ACCEPTED'")
    List<Collaboration> findAcceptedByUser(@Param("userId") Long userId);
} 