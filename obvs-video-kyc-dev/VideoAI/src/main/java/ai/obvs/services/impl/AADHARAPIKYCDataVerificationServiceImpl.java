package ai.obvs.services.impl;

import ai.obvs.dto.Aadhar.GenerateOTPDto;
import ai.obvs.dto.Response;
import ai.obvs.services.KYCDataVerificationService;
import ai.obvs.services.RESTService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.StringUtils;

public class AADHARAPIKYCDataVerificationServiceImpl implements KYCDataVerificationService {
    RESTService restService;

    public AADHARAPIKYCDataVerificationServiceImpl(RESTService restService) {
        this.restService = restService;
    }

    private static final String URL_PREFIX = "https://kyc-api.aadhaarkyc.io";
    private static final String AUTHENTICATION_URL = "/v1/authenticate";
    private static final String PAN_VERIFICATION_URL = "/api/v1/pan/pan";
    private static final String AADHAR_GENERATE_OTP = "/api/v1/aadhaar-v2/generate-otp";
    private static final String AADHAR_CONFIRM_OTP = "/api/v1/aadhaar-v2/submit-otp";

    @Override
    public String getPanDataToVerify(String panCardId) {
        String token = GetToken();

        String url = URL_PREFIX + PAN_VERIFICATION_URL;
        String jsonBodyTemplateData = "{\"id_number\":\"%s\"}";
        String jsonBodyData = String.format(jsonBodyTemplateData, panCardId);
        Response response = restService.postJsonBody(url, token, jsonBodyData);
        return response.getData();
    }

    @Override
    public GenerateOTPDto generateOTPToGetAadharData(String aadharId) {
        String token = GetToken();
        String url = URL_PREFIX + AADHAR_GENERATE_OTP;
        String jsonBodyTemplateData = "{ \"id_number\":\"%s\" }";
        String jsonBodyData = String.format(jsonBodyTemplateData, aadharId);
        Response response = restService.postJsonBody(url, token, jsonBodyData);
        GenerateOTPDto generateOTPDto = new GenerateOTPDto();
        try {
            if (!StringUtils.isEmpty(response)) {
                JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(response.getData());
                int statusCode = Integer.valueOf(responseJsonObject.get("status_code").toString());
                generateOTPDto.setStatus_code(statusCode);

                generateOTPDto.setMessage_code(responseJsonObject.get("message_code").toString());
                generateOTPDto.setMessage(responseJsonObject.get("message").toString());
                generateOTPDto.setSuccess(Boolean.valueOf(responseJsonObject.get("success").toString()));

                if (statusCode == 200) {
                    JSONObject data = (JSONObject) responseJsonObject.get("data");
                    if (data != null) {
                        generateOTPDto.setIf_number(Boolean.valueOf(data.get("if_number").toString()));
                        boolean isValidAadhar = Boolean.valueOf(data.get("valid_aadhaar").toString());
                        boolean isOTPSent = Boolean.valueOf(data.get("otp_sent").toString());
                        generateOTPDto.setValid_aadhaar(isValidAadhar);
                        generateOTPDto.setOtp_sent(isOTPSent);
                        if (isValidAadhar && isOTPSent) {
                            String clientId = data.get("client_id").toString();
                            generateOTPDto.setClientId(clientId);
                        }
                    }
                }
                else{
                    generateOTPDto.setStatus_code(statusCode);
                    generateOTPDto.setMessage("UIADI failed to send the OTP. Please try again.");
                }
            }
        } catch (ParseException e) {
        }
        return generateOTPDto;
    }

    @Override
    public String getAadharDataToVerify(String clientId, String otpValue) {
        String token = GetToken();
        String url = URL_PREFIX + AADHAR_CONFIRM_OTP;
        String jsonBodyTemplateData = "{ \"client_id\":\"%s\",\"otp\":\"%s\" }";
        String jsonBodyData = String.format(jsonBodyTemplateData, clientId, otpValue);
        Response response = restService.postJsonBody(url, token, jsonBodyData);
        return response.getData();
    }

    private String GetToken() {
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MDcwNzUxMjksIm5iZiI6MTYwNzA3NTEyOSwianRpIjoiYTZmOWI1NmEtY2QxNC00MzU3LWIwYzQtMzEyZDk0NWRiZjU5IiwiZXhwIjoxOTIyNDM1MTI5LCJpZGVudGl0eSI6ImRldi5vYnZzQGFhZGhhYXJhcGkuaW8iLCJmcmVzaCI6ZmFsc2UsInR5cGUiOiJhY2Nlc3MiLCJ1c2VyX2NsYWltcyI6eyJzY29wZXMiOlsicmVhZCJdfX0.0dNcQBg1ER-koAxIfjU3yj3EUNyBigak-xwDag5OfIs";
//        String url = URL_PREFIX + AUTHENTICATION_URL;
//        String jsonBodyData = "{\"username\":\"demo\",\"password\":\"demo\"}";
//        Response response = restService.postJsonBody(url, jsonBodyData);
//        try {
//            JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(response.getData());
//            String token = responseJsonObject.get("token").toString();
//            return token;
//        } catch (ParseException e) {
//        }
//        return "";
    }
}
