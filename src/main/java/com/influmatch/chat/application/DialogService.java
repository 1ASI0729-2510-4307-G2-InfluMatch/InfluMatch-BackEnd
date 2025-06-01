package com.influmatch.chat.application;

import com.influmatch.chat.api.CreateDialogRequest;

import com.influmatch.chat.domain.model.Dialog;
import com.influmatch.chat.domain.model.Message;
import com.influmatch.chat.domain.repository.DialogRepository;
import com.influmatch.chat.domain.repository.MessageRepository;
import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.collaboration.domain.repository.CampaignRepository;
import com.influmatch.shared.domain.exception.BusinessException;
import com.influmatch.shared.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DialogService {

    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final CampaignRepository campaignRepository;
    private final DialogEventPublisher eventPublisher;

    public List<Dialog> getUserDialogs(Long userId) {
        return dialogRepository.findByParticipantId(userId);
    }

    public Dialog getDialog(Long dialogId, Long userId) {
        Dialog dialog = dialogRepository.findById(dialogId)
            .orElseThrow(() -> new NotFoundException("dialog_not_found", "El diálogo no existe"));

        if (!dialog.getParticipantIds().contains(userId)) {
            throw new BusinessException("unauthorized_dialog", "No tiene permiso para acceder a este diálogo");
        }

        return dialog;
    }

    public List<Message> getRecentMessages(Long dialogId, Long userId) {
        Dialog dialog = getDialog(dialogId, userId);
        return messageRepository.findRecentByDialogId(dialog.getId(), 20);
    }

    @Transactional
    public Dialog createDialog(CreateDialogRequest request, Long userId) {
        if (request.participantIds() == null || request.participantIds().isEmpty()) {
            throw new BusinessException("participants_required", "Debe especificar los participantes");
        }

        if (request.participantIds().size() < 2) {
            throw new BusinessException("min_two_participants", "Se requieren al menos dos participantes");
        }

        // Ensure userId is included in participants
        var participants = new HashSet<>(request.participantIds());
        participants.add(userId);

        // Create and save dialog
        Dialog dialog = new Dialog(request.campaignId(), participants);
        
        // If campaign ID is provided, verify and set campaign
        if (request.campaignId() != null) {
            Campaign campaign = campaignRepository.findById(request.campaignId())
                .orElseThrow(() -> new NotFoundException("campaign_not_found", "La campaña no existe"));
            dialog.setCampaign(campaign);
        }

        dialog = dialogRepository.save(dialog);
        eventPublisher.publishDialogCreated(dialog);
        return dialog;
    }

    @Transactional
    public void deleteDialog(Long dialogId, Long userId) {
        Dialog dialog = getDialog(dialogId, userId);

        if (messageRepository.existsUnreadInDialog(dialog.getId())) {
            throw new BusinessException(
                "invalid_dialog_state",
                "No se puede eliminar un diálogo con mensajes no leídos"
            );
        }

        messageRepository.deleteByDialogId(dialog.getId());
        dialogRepository.delete(dialog);
        eventPublisher.publishDialogDeleted(dialogId);
    }
} 