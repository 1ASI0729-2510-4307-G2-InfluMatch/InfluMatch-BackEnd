package com.influmatch.collaboration.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_items")
@Getter @Setter
public class ScheduleItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @Column(nullable = false, length = 200)
    private String title;  // Título/descripción de la tarea

    @Column(nullable = false)
    private LocalDate dueDate;  // Fecha límite de entrega

    @Column
    private LocalDateTime doneAt;  // Fecha de completado (null si pendiente)
}

