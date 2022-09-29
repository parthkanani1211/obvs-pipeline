package ai.obvs.services.impl;

import ai.obvs.dto.Aadhar.GenerateOTPDto;
import ai.obvs.dto.Response;
import ai.obvs.services.KYCDataVerificationService;
import ai.obvs.services.RESTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.StringUtils;

public class DBoyzKYCDataVerificationServiceImpl implements KYCDataVerificationService {
    RESTService restService;

    public DBoyzKYCDataVerificationServiceImpl(RESTService restService) {
        this.restService = restService;
    }

    private static final String URL_PREFIX = "https://docboyz.in/docboyzmt/api";
    private static final String AUTHENTICATION_URL = "/v1/authenticate";
    private static final String PAN_VERIFICATION_URL = "/v1/panlite";
    private static final String AADHAR_GENERATE_OTP = "/v1/aadhar/generate-otp";
    private static final String AADHAR_CONFIRM_OTP = "/v1/aadhar/confirm-otp";

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
        String jsonBodyTemplateData = "{ \"aadhar_no\":%s }";
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
            }
        } catch (ParseException e) {
        }
        return generateOTPDto;
    }

    @Override
    public String getAadharDataToVerify(String clientId, String otpValue) {
        String token = GetToken();
        String url = URL_PREFIX + AADHAR_CONFIRM_OTP;
        String jsonBodyTemplateData = "{ \"client_id\":\"%s\",\"otp\":%s }";
        String jsonBodyData = String.format(jsonBodyTemplateData, clientId, otpValue);
        Response response = restService.postJsonBody(url, token, jsonBodyData);
        return response.getData();
    }

    private String GetToken() {
        String url = URL_PREFIX + AUTHENTICATION_URL;
        String jsonBodyData = "{\"username\":\"demo\",\"password\":\"demo\"}";
        Response response = restService.postJsonBody(url, jsonBodyData);
        try {
            JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(response.getData());
            String token = responseJsonObject.get("token").toString();
            return token;
        } catch (ParseException e) {
        }
        return "";
    }
}
