package com.influmatch.auth.application.service;

import com.influmatch.auth.application.assembler.UserAssembler;
import com.influmatch.auth.application.dto.AuthResponse;
import com.influmatch.auth.application.dto.LoginRequest;
import com.influmatch.auth.application.dto.RefreshTokenRequest;
import com.influmatch.auth.application.dto.RegisterRequest;
import com.influmatch.auth.domain.model.User;
import com.influmatch.auth.domain.model.UserRole;
import com.influmatch.auth.domain.model.valueobject.Email;
import com.influmatch.auth.domain.repository.UserRepository;
import com.influmatch.auth.infrastructure.security.CurrentUser;
import com.influmatch.profile.domain.model.entity.BrandProfile;
import com.influmatch.profile.domain.model.entity.InfluencerProfile;
import com.influmatch.profile.domain.repository.BrandProfileRepository;
import com.influmatch.profile.domain.repository.InfluencerProfileRepository;
import com.influmatch.profile.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BrandProfileRepository brandProfileRepository;
    private final InfluencerProfileRepository influencerProfileRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(new Email(request.getEmail()))) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        User user = userAssembler.toEntity(request);
        user = userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        boolean hasProfile = false;
        if (user.getRole() == UserRole.INFLUENCER) {
            hasProfile = influencerProfileRepository.existsByUserId(user.getId());
        } else if (user.getRole() == UserRole.BRAND) {
            hasProfile = brandProfileRepository.existsByUserId(user.getId());
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .profileCompleted(hasProfile)
                .userId(user.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        boolean hasProfile = false;
        String photoUrl = null;
        String name = null;

        if (user.getRole() == UserRole.INFLUENCER) {
            hasProfile = influencerProfileRepository.existsByUserId(user.getId());
            if (hasProfile) {
                Optional<InfluencerProfile> profile = influencerProfileRepository.findByUserId(user.getId());
                if (profile.isPresent()) {
                    photoUrl = fileStorageService.readFileAsBase64(profile.get().getPhotoUrl());
                    name = profile.get().getName();
                }
            }
        } else if (user.getRole() == UserRole.BRAND) {
            hasProfile = brandProfileRepository.existsByUserId(user.getId());
            if (hasProfile) {
                Optional<BrandProfile> profile = brandProfileRepository.findByUserId(user.getId());
                if (profile.isPresent()) {
                    photoUrl = fileStorageService.readFileAsBase64(profile.get().getLogoUrl());
                    name = profile.get().getName();
                }
            }
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .profileCompleted(hasProfile)
                .userId(user.getId())
                .photoUrl(photoUrl)
                .name(name)
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtService.extractUsername(request.getRefreshToken());
        User user = userRepository.findByEmail(new Email(userEmail))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!jwtService.isTokenValid(request.getRefreshToken(), user)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Get profile photo based on user role
        String photoBase64 = null;
        switch (user.getRole()) {
            case BRAND -> {
                BrandProfile profile = brandProfileRepository.findByUserId(user.getId())
                        .orElse(null);
                if (profile != null) {
                    String photoUrl = profile.getProfilePhotoUrl() != null ? 
                                    profile.getProfilePhotoUrl() : 
                                    profile.getLogoUrl();
                    if (photoUrl != null) {
                        photoBase64 = fileStorageService.readFileAsBase64(photoUrl);
                    }
                }
            }
            case INFLUENCER -> {
                InfluencerProfile profile = influencerProfileRepository.findByUserId(user.getId())
                        .orElse(null);
                if (profile != null) {
                    String photoUrl = profile.getProfilePhotoUrl() != null ? 
                                    profile.getProfilePhotoUrl() : 
                                    profile.getPhotoUrl();
                    if (photoUrl != null) {
                        photoBase64 = fileStorageService.readFileAsBase64(photoUrl);
                    }
                }
            }
        }

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .profileCompleted(user.isProfileCompleted())
                .userId(user.getId())
                .photoUrl(photoBase64)
                .build();
    }

    public void logout() {
        // El token ya está invalidado por el filtro JWT
        // No necesitamos hacer nada adicional
    }
} 