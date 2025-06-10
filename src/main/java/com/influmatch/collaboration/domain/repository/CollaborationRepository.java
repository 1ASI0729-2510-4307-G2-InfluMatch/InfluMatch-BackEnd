package com.influmatch.collaboration.domain.repository;

import com.influmatch.collaboration.domain.model.entity.Collaboration;
import com.influmatch.collaboration.domain.model.valueobject.CollaborationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {
    @Query("SELECT c FROM Collaboration c WHERE c.initiatorId = :userId OR c.counterpartId = :userId")
    Page<Collaboration> findAllByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c FROM Collaboration c WHERE (c.initiatorId = :userId OR c.counterpartId = :userId) AND c.status = :status")
    Page<Collaboration> findAllByUserAndStatus(@Param("userId") Long userId, @Param("status") CollaborationStatus status, Pageable pageable);

    @Query("SELECT c FROM Collaboration c WHERE (c.initiatorId = :userId OR c.counterpartId = :userId) AND c.status = 'ACCEPTED'")
    List<Collaboration> findAcceptedByUser(@Param("userId") Long userId);
} 