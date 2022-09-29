package ai.obvs.services.notification;

public interface NotificationService {
    String sendVideoKYCInitURL(Long mobileNumber, String refId);
    void sendOTP(Long mobileNumber, int otpCode);
    void notifyVKYCCompleted(Long mobileNumber);
    void notifyVKYCApproved(Long mobileNumber);
    void notifyVKYCRejected(Long mobileNumber);
}
