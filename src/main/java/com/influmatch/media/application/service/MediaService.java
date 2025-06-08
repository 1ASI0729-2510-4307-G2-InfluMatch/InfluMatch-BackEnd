package com.influmatch.media.application.service;

import com.influmatch.media.domain.model.MediaFile;
import com.influmatch.media.domain.repository.MediaRepository;
import com.influmatch.media.api.dto.MediaUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;

    @Transactional
    public MediaFile uploadFile(MediaUploadRequest request, Long userId) {
        byte[] content = Base64.getDecoder().decode(request.getBase64Content());
        
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFilename(request.getFilename());
        mediaFile.setContentType(request.getContentType());
        mediaFile.setContent(content);
        mediaFile.setSize((long) content.length);
        mediaFile.setType(request.getType());
        mediaFile.setDescription(request.getDescription());
        mediaFile.setTitle(request.getTitle());
        mediaFile.setUploadedBy(userId);

        return mediaRepository.save(mediaFile);
    }

    @Transactional(readOnly = true)
    public MediaFile getFile(Long id) {
        return mediaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found"));
    }
} 