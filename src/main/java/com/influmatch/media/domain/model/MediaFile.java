package com.influmatch.media.domain.model;

import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "media_files")
@Getter @Setter
public class MediaFile extends BaseEntity {

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

    @Lob
    @Column(nullable = false)
    private byte[] content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    @Column(length = 1000)
    private String description;

    // Metadata opcional
    private String title;
    private Integer width;
    private Integer height;
    private Integer duration;  // en segundos, para videos

    @Column(nullable = false)
    private Long uploadedBy;  // ID del usuario que subió el archivo
} 