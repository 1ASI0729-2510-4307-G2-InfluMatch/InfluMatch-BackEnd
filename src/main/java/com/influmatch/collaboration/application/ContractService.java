package com.influmatch.collaboration.application;

import com.influmatch.collaboration.api.CreateContractRequest;
import com.influmatch.collaboration.application.exceptions.ContractAlreadyExistsException;
import com.influmatch.collaboration.application.exceptions.ContractNotFoundException;
import com.influmatch.collaboration.application.exceptions.InvalidContractStateException;
import com.influmatch.collaboration.application.exceptions.NotAuthorizedCampaignException;
import com.influmatch.collaboration.domain.model.Campaign;
import com.influmatch.collaboration.domain.model.CampaignStatus;
import com.influmatch.collaboration.domain.model.Contract;
import com.influmatch.collaboration.domain.repository.CampaignRepository;
import com.influmatch.collaboration.domain.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepo;
    private final CampaignRepository campaignRepo;

    @Transactional(readOnly = true)
    public Contract getContractByCampaignId(Long campaignId) {
        return contractRepo.findByCampaignId(campaignId)
            .orElseThrow(ContractNotFoundException::new);
    }

    @Transactional
    public Contract createContract(Long campaignId, CreateContractRequest request, Long brandId) {
        // Verificar que la campaña existe y pertenece a la marca
        Campaign campaign = campaignRepo.findById(campaignId)
            .orElseThrow(ContractNotFoundException::new);

        if (!Objects.equals(campaign.getBrandId(), brandId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Verificar que no existe un contrato previo
        if (contractRepo.existsByCampaignId(campaignId)) {
            throw new ContractAlreadyExistsException();
        }

        // Crear nuevo contrato
        Contract contract = new Contract();
        contract.setCampaign(campaign);
        contract.setTermsUrl(request.termsUrl());

        return contractRepo.save(contract);
    }

    @Transactional
    public Contract signContract(Long campaignId, Long userId, boolean isBrand) {
        Contract contract = getContractByCampaignId(campaignId);
        Campaign campaign = contract.getCampaign();

        // Verificar autorización
        if (isBrand && !Objects.equals(campaign.getBrandId(), userId)) {
            throw new NotAuthorizedCampaignException();
        } else if (!isBrand && !Objects.equals(campaign.getInfluencerId(), userId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Registrar firma según el rol
        LocalDateTime now = LocalDateTime.now();
        if (isBrand) {
            contract.setSignedBrandAt(now);
        } else {
            contract.setSignedInflAt(now);
        }

        // Si ambos firmaron, activar la campaña
        if (contract.getSignedBrandAt() != null && contract.getSignedInflAt() != null) {
            campaign.setStatus(CampaignStatus.ACTIVE);
            campaignRepo.save(campaign);
        }

        return contractRepo.save(contract);
    }

    @Transactional
    public void deleteContract(Long campaignId, Long brandId) {
        Contract contract = getContractByCampaignId(campaignId);
        Campaign campaign = contract.getCampaign();

        // Verificar autorización
        if (!Objects.equals(campaign.getBrandId(), brandId)) {
            throw new NotAuthorizedCampaignException();
        }

        // Verificar que se puede eliminar
        if (contract.getSignedBrandAt() != null || contract.getSignedInflAt() != null) {
            if (campaign.getStatus() != CampaignStatus.CANCELED) {
                throw new InvalidContractStateException();
            }
        }

        contractRepo.delete(contract);
    }
} 