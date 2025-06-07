package com.influmatch.profiles.infrastructure.security;

import com.influmatch.profiles.application.InfluencerService;
import com.influmatch.profiles.domain.model.InfluencerProfile;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("influencerSecurity")
@RequiredArgsConstructor
public class InfluencerSecurity {

    private final InfluencerService service;

    public boolean isProfileOwner(Long profileId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long currentUserId = Long.parseLong(auth.getName()); // El ID del usuario está en el Name del token
            
            InfluencerProfile profile = service.findById(profileId);
            return profile.getUser().getId().equals(currentUserId);
        } catch (EntityNotFoundException e) {
            // Si el perfil no existe, verificamos si el ID del usuario coincide
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long currentUserId = Long.parseLong(auth.getName());
            return profileId.equals(currentUserId);
        }
    }
} 