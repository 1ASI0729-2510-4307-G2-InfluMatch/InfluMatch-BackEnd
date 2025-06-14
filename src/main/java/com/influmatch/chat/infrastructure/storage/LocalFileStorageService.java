package com.influmatch.chat.infrastructure.storage;

import com.influmatch.profile.domain.model.entity.StoredFile;
import com.influmatch.profile.infrastructure.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {
    private final StoredFileRepository storedFileRepository;

    @Override
    public String storeFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                fileName = "file_" + System.currentTimeMillis();
            }
            StoredFile storedFile = new StoredFile(fileName, file.getContentType(), file.getBytes());
            StoredFile savedFile = storedFileRepository.save(storedFile);
            return String.valueOf(savedFile.getId());
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    @Override
    public String storeFile(byte[] fileContent, String mimeType) {
        String extension = mimeType.split("/")[1];
        String fileName = java.util.UUID.randomUUID().toString() + "." + extension;
        
        StoredFile storedFile = new StoredFile(fileName, mimeType, fileContent);
        StoredFile savedFile = storedFileRepository.save(storedFile);
        return String.valueOf(savedFile.getId());
    }

    @Override
    public void deleteFile(String fileId) {
        try {
            Long id = Long.parseLong(fileId);
            storedFileRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid file ID format: " + fileId);
        }
    }

    @Override
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

  
} 