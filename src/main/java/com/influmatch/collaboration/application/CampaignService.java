package com.influmatch.collaboration.application;

import com.influmatch.collaboration.api.CreateCampaignRequest;
import com.influmatch.collaboration.api.UpdateCampaignRequest;
import com.influmatch.collaboration.application.exceptions.CampaignNotFoundException;
import com.influmatch.collaboration.application.exceptions.InvalidCampaignStateException;
import com.influmatch.collaboration.application.exceptions.NotAuthorizedCampaignException;
import com.influmatch.collaboration.domain.assembler.CampaignAssembler;
import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.collaboration.domain.model.CampaignStatus;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignTitle;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignBrief;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignPeriod;
import com.influmatch.collaboration.domain.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepo;

    @Transactional(readOnly = true)
    public List<Campaign> getAllCampaigns() {
        return campaignRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Campaign getCampaignById(Long id) {
        return campaignRepo.findById(id)
            .orElseThrow(CampaignNotFoundException::new);
    }

    @Transactional
    public Campaign createCampaign(CreateCampaignRequest request, Long brandId) {
        Campaign campaign = CampaignAssembler.toEntity(request, brandId);
        return campaignRepo.save(campaign);
    }

    @Transactional
    public Campaign updateCampaign(Long id, UpdateCampaignRequest request, Long brandId) {
        Campaign campaign = getCampaignById(id);

        // Verificar autorización
        if (!Objects.equals(campaign.getBrandId(), brandId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Verificar estado
        if (campaign.getStatus() == CampaignStatus.FINISHED) {
            throw new InvalidCampaignStateException();
        }

        // Actualizar campos si están presentes
        if (request.title() != null) {
            campaign.setTitle(CampaignTitle.of(request.title()));
        }
        if (request.brief() != null) {
            campaign.setBrief(CampaignBrief.of(request.brief()));
        }
        if (request.status() != null) {
            campaign.setStatus(request.status());
        }
        if (request.startDate() != null || request.endDate() != null) {
            campaign.setPeriod(CampaignPeriod.of(
                request.startDate() != null ? request.startDate() : campaign.getPeriod().startDate(),
                request.endDate() != null ? request.endDate() : campaign.getPeriod().endDate()
            ));
        }

        return campaignRepo.save(campaign);
    }

    @Transactional
    public void deleteCampaign(Long id, Long brandId) {
        Campaign campaign = getCampaignById(id);

        // Verificar autorización
        if (!Objects.equals(campaign.getBrandId(), brandId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Verificar estado
        if (campaign.getStatus() == CampaignStatus.ACTIVE || 
            campaign.getStatus() == CampaignStatus.FINISHED) {
            throw new InvalidCampaignStateException();
        }

        campaignRepo.delete(campaign);
    }
} 