package com.influmatch.profiles.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class MediaAsset extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "infl_id")
    private InfluencerProfile influencer;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private MediaTypeEnum mediaType;

    private String title;
    private String description;
    private Long   sizeBytes;

    @Column(columnDefinition = "jsonb")
    private String metadata;                  // usa String o Map→Json con @Type

    @CreationTimestamp
    private Instant uploadedAt;
}
