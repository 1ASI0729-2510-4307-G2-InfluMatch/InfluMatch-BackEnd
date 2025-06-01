package com.influmatch.collaboration.application;

import com.influmatch.collaboration.api.CreateScheduleItemRequest;
import com.influmatch.collaboration.api.UpdateScheduleItemRequest;
import com.influmatch.collaboration.application.exceptions.InvalidScheduleItemStateException;
import com.influmatch.collaboration.application.exceptions.NotAuthorizedCampaignException;
import com.influmatch.collaboration.application.exceptions.ScheduleItemNotFoundException;
import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.collaboration.domain.model.CampaignStatus;
import com.influmatch.collaboration.domain.model.ScheduleItem;
import com.influmatch.collaboration.domain.repository.CampaignRepository;
import com.influmatch.collaboration.domain.repository.ScheduleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleItemService {

    private final ScheduleItemRepository scheduleItemRepo;
    private final CampaignRepository campaignRepo;

    @Transactional(readOnly = true)
    public List<ScheduleItem> getScheduleItemsByCampaignId(Long campaignId) {
        return scheduleItemRepo.findByCampaignId(campaignId);
    }

    @Transactional
    public ScheduleItem createScheduleItem(Long campaignId, CreateScheduleItemRequest request, Long userId) {
        // Verificar que la campaña existe y el usuario es participante
        Campaign campaign = campaignRepo.findById(campaignId)
            .orElseThrow(ScheduleItemNotFoundException::new);

        if (!isParticipant(campaign, userId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Crear nuevo ítem
        ScheduleItem item = new ScheduleItem();
        item.setCampaign(campaign);
        item.setTitle(request.title());
        item.setDueDate(request.dueDate());

        return scheduleItemRepo.save(item);
    }

    @Transactional
    public ScheduleItem updateScheduleItem(Long itemId, UpdateScheduleItemRequest request, Long userId) {
        ScheduleItem item = scheduleItemRepo.findById(itemId)
            .orElseThrow(ScheduleItemNotFoundException::new);

        Campaign campaign = item.getCampaign();

        // Verificar autorización
        if (!isParticipant(campaign, userId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Actualizar campos si están presentes
        if (request.title() != null) item.setTitle(request.title());
        if (request.dueDate() != null) item.setDueDate(request.dueDate());
        if (request.doneAt() != null) item.setDoneAt(request.doneAt());

        return scheduleItemRepo.save(item);
    }

    @Transactional
    public void deleteScheduleItem(Long itemId, Long userId) {
        ScheduleItem item = scheduleItemRepo.findById(itemId)
            .orElseThrow(ScheduleItemNotFoundException::new);

        Campaign campaign = item.getCampaign();

        // Verificar autorización
        if (!isParticipant(campaign, userId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Verificar que se puede eliminar
        if (item.getDoneAt() != null && campaign.getStatus() != CampaignStatus.CANCELED) {
            throw new InvalidScheduleItemStateException();
        }

        scheduleItemRepo.delete(item);
    }

    private boolean isParticipant(Campaign campaign, Long userId) {
        return Objects.equals(campaign.getBrandId(), userId) || 
               Objects.equals(campaign.getInfluencerId(), userId);
    }
} 