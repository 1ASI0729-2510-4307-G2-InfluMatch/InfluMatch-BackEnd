package com.influmatch.collaboration.domain.model;

import com.influmatch.profiles.domain.model.BrandProfile;
import com.influmatch.profiles.domain.model.InfluencerProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                   // → campaigns.id

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

    @CreationTimestamp private Instant createdAt;
    @UpdateTimestamp  private Instant updatedAt;
}
