package ai.obvs.dto;

import ai.obvs.services.impl.OTPCode;

public class LoginResponse {
    private boolean isUserExists;
    private OTPCode otpCode;

    public boolean isUserExists() {
        return isUserExists;
    }

    public void setUserExists(boolean userExists) {
        isUserExists = userExists;
    }

    public OTPCode getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(OTPCode otpCode) {
        this.otpCode = otpCode;
    }
}
