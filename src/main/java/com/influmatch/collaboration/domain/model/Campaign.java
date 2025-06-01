package com.influmatch.collaboration.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "campaigns")
@Getter @Setter
public class Campaign extends BaseEntity {

    @Column(nullable = false)
    private Long brandId;  // ID de la marca que crea la campaña

    @Column(nullable = false)
    private Long influencerId;  // ID del influencer seleccionado

    @Column(nullable = false, length = 100)
    private String title;  // Título de la campaña

    @Column(length = 2000)
    private String brief;  // Descripción/brief de la campaña

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status = CampaignStatus.DRAFT;

    @Column(nullable = false)
    private LocalDate startDate;  // Fecha de inicio

    @Column(nullable = false)
    private LocalDate endDate;    // Fecha de fin
}
