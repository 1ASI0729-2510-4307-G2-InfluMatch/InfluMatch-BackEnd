package com.influmatch.messaging.domain.model;

import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
public class Dialog extends BaseEntity {

    /** Dialogo puede estar asociado a una campaña (o ser independiente) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")         // FK nullable
    private Campaign campaign;

}