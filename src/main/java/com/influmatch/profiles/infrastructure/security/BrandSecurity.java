package com.influmatch.profiles.infrastructure.security;

import com.influmatch.profiles.application.BrandService;
import com.influmatch.profiles.domain.model.BrandProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("brandSecurity")
@RequiredArgsConstructor
public class BrandSecurity {

    private final BrandService service;

    public boolean isProfileOwner(Long profileId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = Long.parseLong(auth.getName()); // El ID del usuario está en el Name del token
        
        BrandProfile profile = service.findById(profileId);
        return profile.getUser().getId().equals(currentUserId);
    }
} 