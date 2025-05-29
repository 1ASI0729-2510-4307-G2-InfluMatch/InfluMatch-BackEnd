package com.influmatch.notifications.domain.repository;

import com.influmatch.notifications.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}
