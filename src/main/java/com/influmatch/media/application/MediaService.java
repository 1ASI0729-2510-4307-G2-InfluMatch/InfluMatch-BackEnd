package com.influmatch.media.application;

import com.influmatch.media.api.CreateMediaRequest;
import com.influmatch.media.api.UpdateMediaRequest;
import com.influmatch.media.application.exceptions.MediaNotFoundException;
import com.influmatch.media.application.exceptions.NotAuthorizedMediaException;
import com.influmatch.media.application.exceptions.UnsupportedMediaTypeException;
import com.influmatch.media.domain.model.MediaFile;
import com.influmatch.media.domain.model.MediaType;
import com.influmatch.media.domain.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepo;
    private final StorageService storage;

    // Tipos MIME permitidos y su correspondiente MediaType
    private static final Map<String, MediaType> ALLOWED_TYPES = Map.of(
        "image/jpeg", MediaType.IMAGE,
        "image/png", MediaType.IMAGE,
        "image/gif", MediaType.IMAGE,
        "video/mp4", MediaType.VIDEO,
        "video/quicktime", MediaType.VIDEO,
        "application/pdf", MediaType.DOCUMENT,
        "application/msword", MediaType.DOCUMENT,
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", MediaType.DOCUMENT
    );

    @Transactional
    public MediaFile createMedia(CreateMediaRequest request, Long userId) {
        MultipartFile file = request.file();
        String contentType = file.getContentType();

        // Validar tipo de archivo
        if (!ALLOWED_TYPES.containsKey(contentType)) {
            throw new UnsupportedMediaTypeException();
        }

        try {
            // Procesar archivo
            String filename = storage.store(file);

            // Crear registro en BD
            MediaFile asset = new MediaFile();
            asset.setFilename(filename);
            asset.setContentType(contentType);
            asset.setSize(file.getSize());
            asset.setContent(file.getBytes());
            asset.setType(ALLOWED_TYPES.get(contentType));
            asset.setTitle(request.title());
            asset.setDescription(request.description());
            asset.setUploadedBy(userId);

            // Si es imagen o video, extraer dimensiones
            if (Set.of(MediaType.IMAGE, MediaType.VIDEO).contains(asset.getType())) {
                var metadata = storage.extractMetadata(file);
                asset.setWidth(metadata.width());
                asset.setHeight(metadata.height());
                if (asset.getType() == MediaType.VIDEO) {
                    asset.setDuration(metadata.duration());
                }
            }

            return mediaRepo.save(asset);

        } catch (IOException e) {
            log.error("Error al procesar archivo multimedia", e);
            throw new RuntimeException("Error al procesar el archivo");
        }
    }

    @Transactional(readOnly = true)
    public MediaFile getMediaById(Long id) {
        return mediaRepo.findById(id)
            .orElseThrow(MediaNotFoundException::new);
    }

    @Transactional
    public MediaFile updateMedia(Long id, UpdateMediaRequest request, Long userId) {
        MediaFile asset = getMediaById(id);

        // Verificar autorización
        if (!asset.getUploadedBy().equals(userId)) {
            throw new NotAuthorizedMediaException();
        }

        // Actualizar solo campos editables
        if (request.title() != null) {
            asset.setTitle(request.title());
        }
        if (request.description() != null) {
            asset.setDescription(request.description());
        }

        return mediaRepo.save(asset);
    }

    @Transactional
    public void deleteMedia(Long id, Long userId) {
        MediaFile asset = getMediaById(id);

        // Verificar autorización
        if (!asset.getUploadedBy().equals(userId)) {
            throw new NotAuthorizedMediaException();
        }

        // Eliminar archivo del almacenamiento
        storage.delete(asset.getFilename());

        // Eliminar registro de BD
        mediaRepo.delete(asset);
    }
} 