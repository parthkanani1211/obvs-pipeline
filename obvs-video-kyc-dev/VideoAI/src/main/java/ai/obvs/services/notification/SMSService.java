package ai.obvs.services.notification;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface SMSService {
    void sendOTP(Long mobileNumber, int otpNumber) throws IOException, GeneralSecurityException;
    void sendMessage(Long mobileNumber, String message);
}
