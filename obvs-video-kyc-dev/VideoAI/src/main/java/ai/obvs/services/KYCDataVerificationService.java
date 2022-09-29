package ai.obvs.services;

import ai.obvs.dto.Aadhar.GenerateOTPDto;

public interface KYCDataVerificationService {
    String getPanDataToVerify(String panId);

    GenerateOTPDto generateOTPToGetAadharData(String aadharId);

    String getAadharDataToVerify(String clientId, String otpValue);
}
