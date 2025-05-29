package com.influmatch.profiles.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class InfluencerSocialLink {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                           // → influencer_social_links.id

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "infl_id")
    private InfluencerProfile influencer;

    @Enumerated(EnumType.STRING)
    private PlatformEnum platform;

    @Column(nullable = false)
    private String url;
}
