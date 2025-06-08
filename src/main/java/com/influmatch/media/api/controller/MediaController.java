package com.influmatch.media.api.controller;

import com.influmatch.media.api.dto.MediaUploadRequest;
import com.influmatch.media.application.service.MediaService;
import com.influmatch.media.domain.model.MediaFile;
import com.influmatch.shared.api.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MediaFile>> uploadFile(
            @RequestBody MediaUploadRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = Long.parseLong(userDetails.getUsername());
        MediaFile mediaFile = mediaService.uploadFile(request, userId);
        
        return ResponseEntity.ok(new ApiResponse<>(mediaFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaFile>> getFile(
            @PathVariable Long id) {
        
        MediaFile mediaFile = mediaService.getFile(id);
        return ResponseEntity.ok(new ApiResponse<>(mediaFile));
    }
} 