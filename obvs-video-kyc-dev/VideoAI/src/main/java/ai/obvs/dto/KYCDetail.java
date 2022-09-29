package ai.obvs.dto;

public class KYCDetail {
    private KYCVerificationData kycVerificationData;
//    private boolean isVerified;

    public KYCVerificationData getKycVerificationData() {
        return kycVerificationData;
    }

    public void setKycVerificationData(KYCVerificationData kycVerificationData) {
        this.kycVerificationData = kycVerificationData;
    }

//    public boolean isVerified() {
//        return isVerified;
//    }
//
//    public void setVerified(boolean verified) {
//        isVerified = verified;
//    }
}
