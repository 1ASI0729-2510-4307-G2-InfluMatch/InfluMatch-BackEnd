package com.influmatch.collaboration.domain.assembler;

import com.influmatch.collaboration.api.CreateCampaignRequest;
import com.influmatch.collaboration.api.CampaignResponse;
import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignTitle;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignBrief;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignPeriod;

public class CampaignAssembler {
    
    public static Campaign toEntity(CreateCampaignRequest request, Long brandId) {
        Campaign campaign = new Campaign();
        campaign.setBrandId(brandId);
        campaign.setInfluencerId(request.influencerId());
        campaign.setTitle(CampaignTitle.of(request.title()));
        campaign.setBrief(request.brief() != null ? CampaignBrief.of(request.brief()) : null);
        campaign.setPeriod(CampaignPeriod.of(request.startDate(), request.endDate()));
        return campaign;
    }

    public static CampaignResponse toResponse(Campaign campaign) {
        return new CampaignResponse(
            campaign.getId(),
            campaign.getBrandId(),
            campaign.getInfluencerId(),
            campaign.getTitle().toString(),
            campaign.getBrief() != null ? campaign.getBrief().toString() : null,
            campaign.getStatus(),
            campaign.getPeriod().startDate(),
            campaign.getPeriod().endDate(),
            campaign.getCreatedAt(),
            campaign.getUpdatedAt()
        );
    }
} 