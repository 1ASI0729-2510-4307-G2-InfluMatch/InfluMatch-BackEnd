package com.influmatch.collaboration.domain.model;

import com.influmatch.profiles.domain.model.BrandProfile;
import com.influmatch.profiles.domain.model.InfluencerProfile;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor
public class Campaign extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private BrandProfile brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "infl_id")
    private InfluencerProfile influencer;

    @Column(nullable = false)
    private String title;

    private String brief;

    @Enumerated(EnumType.STRING)
    private CampaignStatusEnum status = CampaignStatusEnum.DRAFT;

    private LocalDate startDate;
    private LocalDate endDate;

}
