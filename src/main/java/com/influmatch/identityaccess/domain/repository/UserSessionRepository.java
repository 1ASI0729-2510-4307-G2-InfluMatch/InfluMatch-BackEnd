package com.influmatch.identityaccess.domain.repository;

import com.influmatch.identityaccess.domain.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByRefreshToken(String refreshToken);
    
    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.user.id = :userId")
    void deleteAllByUserId(Long userId);
} 