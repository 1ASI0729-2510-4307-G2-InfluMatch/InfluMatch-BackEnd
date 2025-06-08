package com.influmatch.profiles.api.controller;

import com.influmatch.profiles.api.dto.CreateBrandProfileRequest;
import com.influmatch.profiles.api.dto.CreateInfluencerProfileRequest;
import com.influmatch.profiles.application.service.ProfileService;
import com.influmatch.profiles.domain.model.BrandProfile;
import com.influmatch.profiles.domain.model.InfluencerProfile;
import com.influmatch.shared.api.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/brand")
    public ResponseEntity<ApiResponse<BrandProfile>> createBrandProfile(
            @RequestBody CreateBrandProfileRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = Long.parseLong(userDetails.getUsername());
        BrandProfile profile = profileService.createBrandProfile(request, userId);
        
        return ResponseEntity.ok(new ApiResponse<>(profile));
    }

    @PostMapping("/influencer")
    public ResponseEntity<ApiResponse<InfluencerProfile>> createInfluencerProfile(
            @RequestBody CreateInfluencerProfileRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = Long.parseLong(userDetails.getUsername());
        InfluencerProfile profile = profileService.createInfluencerProfile(request, userId);
        
        return ResponseEntity.ok(new ApiResponse<>(profile));
    }
} 