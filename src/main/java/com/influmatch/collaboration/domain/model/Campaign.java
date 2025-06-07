package com.influmatch.collaboration.domain.model;

import com.influmatch.collaboration.domain.model.valueObjects.CampaignTitle;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignBrief;
import com.influmatch.collaboration.domain.model.valueObjects.CampaignPeriod;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "campaigns")
@Getter @Setter
public class Campaign extends BaseEntity {

    @Column(nullable = false)
    private Long brandId;  // ID de la marca que crea la campaña

    @Column(nullable = false)
    private Long influencerId;  // ID del influencer seleccionado

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title", nullable = false, length = 100))
    private CampaignTitle title;  // Título de la campaña

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "brief", length = 2000))
    private CampaignBrief brief;  // Descripción/brief de la campaña

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status = CampaignStatus.DRAFT;

    @Embedded
    private CampaignPeriod period;  // Período de la campaña
}
