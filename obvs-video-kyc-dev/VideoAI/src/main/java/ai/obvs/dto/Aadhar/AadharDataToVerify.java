package ai.obvs.dto.Aadhar;

import ai.obvs.dto.Aadhar.AadharValidationInfo;
import ai.obvs.dto.KYCVerificationData;

public class AadharDataToVerify {
    private String clientId;
    private String otpValue;
    private KYCVerificationData kycVerificationData;
    private AadharValidationInfo aadharValidationInfo;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public KYCVerificationData getKycVerificationData() {
        return kycVerificationData;
    }

    public void setKycVerificationData(KYCVerificationData kycVerificationData) {
        this.kycVerificationData = kycVerificationData;
    }

    public AadharValidationInfo getAadharValidationInfo() {
        return aadharValidationInfo;
    }

    public void setAadharValidationInfo(AadharValidationInfo aadharValidationInfo) {
        this.aadharValidationInfo = aadharValidationInfo;
    }
}
