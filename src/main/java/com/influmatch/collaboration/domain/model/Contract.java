package com.influmatch.collaboration.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Getter @Setter
public class Contract extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false, unique = true)
    private Campaign campaign;

    @Column(nullable = false)
    private String termsUrl;  // URL al documento con términos del contrato

    @Column
    private LocalDateTime signedBrandAt;  // Fecha de firma de la marca

    @Column
    private LocalDateTime signedInflAt;   // Fecha de firma del influencer
}
