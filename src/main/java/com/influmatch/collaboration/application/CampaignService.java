package com.influmatch.collaboration.application;

import com.influmatch.collaboration.api.CreateCampaignRequest;
import com.influmatch.collaboration.api.UpdateCampaignRequest;
import com.influmatch.collaboration.application.exceptions.CampaignNotFoundException;
import com.influmatch.collaboration.application.exceptions.InvalidCampaignStateException;
import com.influmatch.collaboration.application.exceptions.NotAuthorizedCampaignException;
import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.collaboration.domain.model.CampaignStatus;
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
        Campaign campaign = new Campaign();
        campaign.setBrandId(brandId);
        campaign.setInfluencerId(request.influencerId());
        campaign.setTitle(request.title());
        campaign.setBrief(request.brief());
        campaign.setStartDate(request.startDate());
        campaign.setEndDate(request.endDate());
        
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
        if (request.title() != null) campaign.setTitle(request.title());
        if (request.brief() != null) campaign.setBrief(request.brief());
        if (request.status() != null) campaign.setStatus(request.status());
        if (request.startDate() != null) campaign.setStartDate(request.startDate());
        if (request.endDate() != null) campaign.setEndDate(request.endDate());

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