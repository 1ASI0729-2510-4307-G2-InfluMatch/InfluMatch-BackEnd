package com.influmatch.chat.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    String storeFile(byte[] fileContent, String mimeType);
    void deleteFile(String fileUrl);
    String readFileAsBase64(String fileUrl);
} 