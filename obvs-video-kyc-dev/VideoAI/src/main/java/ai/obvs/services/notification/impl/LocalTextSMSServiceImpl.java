package ai.obvs.services.notification.impl;

import ai.obvs.services.notification.SMSService;
import com.telesign.MessagingClient;
import com.telesign.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;

public class LocalTextSMSServiceImpl implements SMSService {

    private static final String CUSTOMER_ID = "359CC4FC-0232-4D95-BA2D-5F91F7B4BB63";
    private static final String API_KEY = "NTAzNTUzMzk1ODM2Njg2ZDZkNWE0Yzc4Njc3NjQ5NmI=";
    private static final String messageType = "ARN";

    private MessagingClient messagingClient;

    public LocalTextSMSServiceImpl() {
        messagingClient = new MessagingClient(CUSTOMER_ID, API_KEY);
    }

    @Override
    public void sendOTP(Long mobileNumber, int otpNumber) throws IOException, GeneralSecurityException {

        RestClient.TelesignResponse telesignResponse = messagingClient.message(String.valueOf(mobileNumber), String.valueOf(otpNumber), messageType, null);
        System.out.println("Your reference id is: " + telesignResponse.json.get("reference_id"));
    }

    @Override
    public void sendMessage(Long mobileNumber, String message) {
        try {
            // Construct data
            String apiKey = "apikey=" + API_KEY;
            message = "&message=" + message;
//            String sender = "&sender=" + "XTCTCF%20";
            String numbers = "&numbers=" + String.valueOf(mobileNumber);

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message ;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            String s = stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
        }
    }

}
