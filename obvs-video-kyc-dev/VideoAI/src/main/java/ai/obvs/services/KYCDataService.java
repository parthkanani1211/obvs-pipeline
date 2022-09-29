package ai.obvs.services;

import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.Aadhar.AadharDataValidationDto;
import ai.obvs.dto.KYCDto;
import ai.obvs.dto.KYCRequestImageDto;
import ai.obvs.dto.KYCResourcesDto;
import ai.obvs.dto.KYCVerificationData;
import ai.obvs.model.KYC;
import ai.obvs.model.KYCData;

import java.util.List;
import java.util.Optional;

public interface KYCDataService {

    Optional<KYCData> getKYCData(KYC kyc);

    void saveExtractedData(KYC kyc, String extractedData);

    void saveVerificationData(KYC kyc, String verificationData);
}
