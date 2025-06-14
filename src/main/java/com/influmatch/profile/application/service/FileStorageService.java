package com.influmatch.profile.application.service;

import com.influmatch.profile.domain.exception.ProfileException;
import com.influmatch.profile.domain.model.entity.StoredFile;
import com.influmatch.profile.infrastructure.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final StoredFileRepository storedFileRepository;
    private final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");
    private final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of("mp4", "mov", "avi");
    private final Set<String> ALLOWED_DOCUMENT_EXTENSIONS = Set.of("pdf", "doc", "docx");

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ProfileException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ProfileException("Original filename is null");
        }

        String extension = getFileExtension(originalFilename);
        validateFileExtension(extension);

        try {
            String filename = generateUniqueFilename(file);
            StoredFile storedFile = new StoredFile(
                filename,
                file.getContentType(),
                file.getBytes()
            );
            StoredFile savedFile = storedFileRepository.save(storedFile);
            return String.valueOf(savedFile.getId());
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    public String storeBase64File(String base64Data, String extension) {
        if (base64Data == null || base64Data.isEmpty()) {
            throw new ProfileException("Base64 data is empty");
        }

        validateFileExtension(extension);

        try {
            String filename = UUID.randomUUID() + "." + extension;
            String mimeType = getMimeTypeForExtension(extension);
            
            byte[] decodedData = Base64.getDecoder().decode(base64Data);
            StoredFile storedFile = new StoredFile(filename, mimeType, decodedData);
            StoredFile savedFile = storedFileRepository.save(storedFile);
            
            return String.valueOf(savedFile.getId());
        } catch (IllegalArgumentException e) {
            throw new ProfileException("Invalid Base64 data");
        }
    }

    public String readFileAsBase64(String fileId) {
        if (fileId == null || fileId.isEmpty()) {
            return null;
        }

        try {
            Long id = Long.parseLong(fileId);
            StoredFile file = storedFileRepository.findById(id)
                .orElse(null);
            if (file == null) {
                return null;
            }
            return Base64.getEncoder().encodeToString(file.getData());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String generateUniqueFilename(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID() + "." + extension;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new ProfileException("File has no extension");
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private void validateFileExtension(String extension) {
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension) &&
            !ALLOWED_VIDEO_EXTENSIONS.contains(extension) &&
            !ALLOWED_DOCUMENT_EXTENSIONS.contains(extension)) {
            throw new ProfileException("File extension not allowed: " + extension);
        }
    }

    private String getMimeTypeForExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "mp4" -> "video/mp4";
            case "mov" -> "video/quicktime";
            case "avi" -> "video/x-msvideo";
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/octet-stream";
        };
    }
} 