package com.influmatch.media.domain.repository;

import com.influmatch.media.domain.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<MediaFile, Long> {
} 