package ai.obvs.services.notification.impl;

import ai.obvs.services.notification.SMSService;
import com.telesign.MessagingClient;
import com.telesign.RestClient;
import liquibase.pro.packaged.S;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TelesignSMSServiceImpl {

    private static final String CUSTOMER_ID = "359CC4FC-0232-4D95-BA2D-5F91F7B4BB63";
    private static final String API_KEY = "ghlchFfCf7ZMNlk+1zjfBFW45wGpvfRpCNt5egkstPGikpwIgss4X9+Q4wLEVq0P5dgxw/1N6lA/UK3rBiFaLg==";
    private static final String messageType = "ARN";

    private MessagingClient messagingClient;

    public TelesignSMSServiceImpl() {
        messagingClient = new MessagingClient(CUSTOMER_ID, API_KEY);
    }

    public void sendOTP(Long mobileNumber, int otpNumber) throws IOException, GeneralSecurityException {

        RestClient.TelesignResponse telesignResponse = messagingClient.message(String.valueOf(mobileNumber), String.valueOf(otpNumber), messageType, null);
        System.out.println("Your reference id is: " + telesignResponse.json.get("reference_id"));
    }

    public void sendMessage(Long mobileNumber, String message) throws IOException, GeneralSecurityException {
        RestClient.TelesignResponse telesignResponse = messagingClient.message("918460107732", String.valueOf(message), messageType, null);
        System.out.println("Your reference id is: " + telesignResponse.json.get("reference_id"));
    }

}
