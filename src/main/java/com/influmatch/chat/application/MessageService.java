package com.influmatch.chat.application;

import com.influmatch.chat.api.CreateMessageRequest;
import com.influmatch.chat.domain.model.Dialog;
import com.influmatch.chat.domain.model.Message;
import com.influmatch.chat.domain.repository.DialogRepository;
import com.influmatch.chat.domain.repository.MessageRepository;
import com.influmatch.shared.domain.exception.BusinessException;
import com.influmatch.shared.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final MessageEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public Page<Message> getDialogMessages(Long dialogId, Long userId, Pageable pageable) {
        getDialogWithAuthorization(dialogId, userId);
        return messageRepository.findByDialogIdOrderByCreatedAtDesc(dialogId, pageable);
    }

    @Transactional
    public Message createMessage(Long dialogId, Long userId, CreateMessageRequest request) {
        Dialog dialog = getDialogWithAuthorization(dialogId, userId);
        Message message = new Message(dialog, userId, request.content(), request.assetId());
        message = messageRepository.save(message);
        eventPublisher.publishMessageCreated(message);
        return message;
    }

    @Transactional
    public Message markAsRead(Long dialogId, Long messageId, Long userId) {
        getDialogWithAuthorization(dialogId, userId);
        
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new NotFoundException("message_not_found", "El mensaje no existe"));

        if (!message.getDialog().getId().equals(dialogId)) {
            throw new BusinessException("invalid_dialog", "El mensaje no pertenece a este diálogo");
        }

        if (message.getSenderId().equals(userId)) {
            throw new BusinessException(
                "unauthorized_message",
                "No puede marcar como leído un mensaje propio"
            );
        }

        message.markAsRead();
        message = messageRepository.save(message);

        eventPublisher.publishMessageRead(message);
        return message;
    }

    @Transactional
    public void deleteMessage(Long dialogId, Long messageId, Long userId) {
        getDialogWithAuthorization(dialogId, userId);
        
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new NotFoundException("message_not_found", "El mensaje no existe"));

        if (!message.getDialog().getId().equals(dialogId)) {
            throw new BusinessException("invalid_dialog", "El mensaje no pertenece a este diálogo");
        }

        if (!message.canBeDeletedBy(userId)) {
            throw new BusinessException(
                "unauthorized_message",
                "Solo el autor puede eliminar mensajes no leídos"
            );
        }

        messageRepository.delete(message);
        eventPublisher.publishMessageDeleted(dialogId, messageId);
    }

    private Dialog getDialogWithAuthorization(Long dialogId, Long userId) {
        Dialog dialog = dialogRepository.findById(dialogId)
            .orElseThrow(() -> new NotFoundException("dialog_not_found", "El diálogo no existe"));

        if (!dialog.getParticipantIds().contains(userId)) {
            throw new BusinessException(
                "unauthorized_dialog",
                "No tiene permiso para acceder a este diálogo"
            );
        }

        return dialog;
    }
} 