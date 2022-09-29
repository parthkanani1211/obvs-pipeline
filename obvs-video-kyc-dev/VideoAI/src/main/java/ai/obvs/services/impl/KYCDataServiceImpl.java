package ai.obvs.services.impl;

import ai.obvs.Enums.DocumentName;
import ai.obvs.Enums.KYCResourceType;
import ai.obvs.Enums.WorkflowStepType;
import ai.obvs.dto.Aadhar.AadharDataValidationDto;
import ai.obvs.dto.KYCDto;
import ai.obvs.dto.KYCRequestImageDto;
import ai.obvs.dto.KYCResourcesDto;
import ai.obvs.dto.KYCVerificationData;
import ai.obvs.mapper.KYCMapper;
import ai.obvs.mapper.KYCResourceMapper;
import ai.obvs.model.*;
import ai.obvs.repository.KYCDataRepository;
import ai.obvs.repository.KYCRepository;
import ai.obvs.repository.ResourceRepository;
import ai.obvs.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class KYCDataServiceImpl implements KYCDataService {

    private KYCDataRepository kycDataRepository;

    public KYCDataServiceImpl(KYCDataRepository kycDataRepository) {
        this.kycDataRepository = kycDataRepository;
    }

    @Override
    public Optional<KYCData> getKYCData(KYC kyc) {
        Optional<KYCData> kycDataOptional = kycDataRepository.findByKycId(kyc.getId());
        if (kycDataOptional.isPresent()) {
            return Optional.of(kycDataOptional.get());
        }
        return Optional.empty();
    }

    @Override
    public void saveExtractedData(KYC kyc, String extractedData) {
        Optional<KYCData> kycDataOptional = kycDataRepository.findByKycId(kyc.getId());
        KYCData kycData;
        if (kycDataOptional.isPresent())
            kycData = kycDataOptional.get();
        else
            kycData = new KYCData();

        kycData.setExtractedData(extractedData);
        kycData.setKyc(kyc);
        kycDataRepository.save(kycData);
    }

    @Override
    public void saveVerificationData(KYC kyc, String verificationData) {
        Optional<KYCData> kycDataOptional = kycDataRepository.findByKycId(kyc.getId());
        if (kycDataOptional.isPresent()) {
            KYCData kycData = kycDataOptional.get();
            kycData.setVerificationData(verificationData);
            kycDataRepository.save(kycData);
        }
    }
}
