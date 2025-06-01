package com.influmatch.profiles.application;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.identityaccess.domain.repository.UserRepository;
import com.influmatch.profiles.domain.model.BrandProfile;
import com.influmatch.profiles.domain.repository.BrandProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandProfileRepository brandRepo;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public List<BrandProfile> findAll() {
        return brandRepo.findAll();
    }

    @Transactional(readOnly = true)
    public BrandProfile findById(Long id) {
        return brandRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("brand_not_found"));
    }

    @Transactional
    public BrandProfile create(Long userId, String companyName, String description, 
                             String industry, String websiteUrl, String logoUrl) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("user_not_found"));

        BrandProfile profile = new BrandProfile();
        profile.setUser(user);
        profile.setCompanyName(companyName);
        profile.setDescription(description);
        profile.setIndustry(industry);
        profile.setWebsiteUrl(websiteUrl);
        profile.setLogoUrl(logoUrl);

        return brandRepo.save(profile);
    }

    @Transactional
    public BrandProfile update(Long id, String companyName, String description,
                             String industry, String websiteUrl, String logoUrl) {
        BrandProfile profile = findById(id);

        profile.setCompanyName(companyName);
        profile.setDescription(description);
        profile.setIndustry(industry);
        profile.setWebsiteUrl(websiteUrl);
        profile.setLogoUrl(logoUrl);

        return brandRepo.save(profile);
    }

    @Transactional
    public void delete(Long id) {
        if (!brandRepo.existsById(id)) {
            throw new EntityNotFoundException("brand_not_found");
        }
        brandRepo.deleteById(id);
    }
} 