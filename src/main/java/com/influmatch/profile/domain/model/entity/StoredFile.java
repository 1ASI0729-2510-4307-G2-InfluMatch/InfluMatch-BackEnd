package com.influmatch.profile.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stored_files")
@Getter
@NoArgsConstructor
public class StoredFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String mimeType;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    public StoredFile(String fileName, String mimeType, byte[] data) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.data = data;
    }
} 