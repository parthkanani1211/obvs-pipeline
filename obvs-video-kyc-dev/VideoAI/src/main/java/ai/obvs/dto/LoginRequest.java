package ai.obvs.dto;

public class LoginRequest {
    private Long mobileNumber;
    private String refId;
    private Long otpCode;

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public Long getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(Long otpCode) {
        this.otpCode = otpCode;
    }
}
