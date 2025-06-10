package com.influmatch.profile.application.service;

import com.influmatch.profile.domain.exception.ProfileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadPath;
    private final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");
    private final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of("mp4", "mov", "avi");
    private final Set<String> ALLOWED_DOCUMENT_EXTENSIONS = Set.of("pdf", "doc", "docx");

    public FileStorageService() {
        this.uploadPath = Paths.get("uploads");
        createUploadDirectory();
    }

    private void createUploadDirectory() {
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

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
            Path targetPath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetPath);
            return "/uploads/" + filename;
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
            Path targetPath = uploadPath.resolve(filename);
            
            byte[] decodedData = Base64.getDecoder().decode(base64Data);
            Files.write(targetPath, decodedData);
            
            return "/uploads/" + filename;
        } catch (IllegalArgumentException e) {
            throw new ProfileException("Invalid Base64 data");
        } catch (IOException e) {
            throw new RuntimeException("Could not store base64 file", e);
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
} 