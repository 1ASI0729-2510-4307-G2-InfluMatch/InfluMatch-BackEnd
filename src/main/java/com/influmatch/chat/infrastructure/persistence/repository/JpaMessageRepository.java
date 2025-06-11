package com.influmatch.chat.infrastructure.persistence.repository;

import com.influmatch.chat.infrastructure.persistence.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderByCreatedAtDesc(Long chatId);
} 