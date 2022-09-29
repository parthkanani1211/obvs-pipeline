package ai.obvs.dto.Aadhar;

public class GenerateOTPDto {
    private String clientId;
    private boolean otp_sent;
    private boolean valid_aadhaar;
    private boolean if_number;
    private int status_code;
    private String message_code;
    private String message;
    private boolean success;

    public GenerateOTPDto() {
        this.clientId = "";
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isOtp_sent() {
        return otp_sent;
    }

    public void setOtp_sent(boolean otp_sent) {
        this.otp_sent = otp_sent;
    }

    public boolean isValid_aadhaar() {
        return valid_aadhaar;
    }

    public void setValid_aadhaar(boolean valid_aadhaar) {
        this.valid_aadhaar = valid_aadhaar;
    }

    public boolean isIf_number() {
        return if_number;
    }

    public void setIf_number(boolean if_number) {
        this.if_number = if_number;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage_code() {
        return message_code;
    }

    public void setMessage_code(String message_code) {
        this.message_code = message_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
