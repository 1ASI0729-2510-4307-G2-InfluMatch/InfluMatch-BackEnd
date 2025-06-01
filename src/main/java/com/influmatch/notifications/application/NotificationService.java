package com.influmatch.notifications.application;

import com.influmatch.notifications.api.CreateNotificationRequest;
import com.influmatch.notifications.domain.model.Notification;
import com.influmatch.notifications.domain.repository.NotificationRepository;
import com.influmatch.shared.domain.exception.BusinessException;
import com.influmatch.shared.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public Page<Notification> getUserNotifications(Long userId, boolean unreadOnly, Pageable pageable) {
        if (unreadOnly) {
            return notificationRepository.findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(userId, pageable);
        }
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Notification getNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotFoundException("notification_not_found", "La notificación no existe"));

        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(
                "unauthorized_notification",
                "No tiene permiso para ver esta notificación"
            );
        }

        return notification;
    }

    @Transactional
    public Notification createNotification(CreateNotificationRequest request) {
        validateUserId(request.userId());

        Notification notification = new Notification(
            request.userId(),
            request.type(),
            request.payload()
        );

        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification markAsRead(Long notificationId, Long userId) {
        Notification notification = getNotification(notificationId, userId);
        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        if (!notificationRepository.existsByIdAndUserId(notificationId, userId)) {
            throw new BusinessException(
                "unauthorized_notification",
                "No tiene permiso para eliminar esta notificación"
            );
        }
        notificationRepository.deleteById(notificationId);
    }

    private void validateUserId(Long userId) {
        // Aquí deberías validar que el userId corresponde a un usuario existente
        // Por ahora solo verificamos que no sea null, pero deberías agregar la validación real
        if (userId == null) {
            throw new BusinessException(
                "invalid_user",
                "El ID del usuario es inválido"
            );
        }
    }
} 