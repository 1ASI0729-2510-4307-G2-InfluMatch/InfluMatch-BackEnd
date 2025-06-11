package com.influmatch.chat.infrastructure.persistence.repository;

import com.influmatch.chat.infrastructure.persistence.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserId(Long userId);
    
    @Modifying
    @Query("UPDATE Chat c SET c.unreadCount = ?3 WHERE c.id = ?1 AND c.userId = ?2")
    void updateUnreadCount(Long chatId, Long userId, int unreadCount);
} 