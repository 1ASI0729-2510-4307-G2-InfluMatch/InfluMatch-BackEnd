package com.influmatch.shared.domain.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 *  Entidad base para todo el dominio.
 *
 *  ▸ Contiene:
 *      – id BIGINT autoincremental
 *      – created_at / updated_at  (heredados de {@link TimestampedEntity})
 *
 *  Cualquier entidad de negocio hereda de aquí; así solo
 *  mantenemos un “extends” y centralizamos cambios comunes.
 */
@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
