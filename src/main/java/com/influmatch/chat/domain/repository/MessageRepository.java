package com.influmatch.chat.domain.repository;

import com.influmatch.chat.domain.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query(value = "SELECT m FROM Message m WHERE m.dialog.id = :dialogId " +
           "ORDER BY m.createdAt DESC LIMIT :limit")
    List<Message> findRecentByDialogId(Long dialogId, int limit);

    @Query("SELECT COUNT(m) > 0 FROM Message m WHERE m.dialog.id = :dialogId AND m.readAt IS NULL")
    boolean existsUnreadInDialog(Long dialogId);

    void deleteByDialogId(Long dialogId);
} 