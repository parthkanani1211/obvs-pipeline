package ai.obvs.services;

import ai.obvs.dto.Aadhar.AadharAddress;
import ai.obvs.dto.Aadhar.AadharValidationInfo;
import ai.obvs.dto.Aadhar.AadharVerificationData;
import ai.obvs.dto.Aadhar.GenerateOTPDto;
import ai.obvs.integration.models.AadharResponse;
import ai.obvs.model.KYC;
import ai.obvs.model.KYCData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.StringUtils;

import java.util.*;

public class AadharCardDataVerificationService {

    private KYCDataVerificationService kycDataVerificationService;
    private KYCDataService kycDataService;

    public AadharCardDataVerificationService(KYCDataVerificationService kycDataVerificationService,
                                             KYCDataService kycDataService) {
        this.kycDataVerificationService = kycDataVerificationService;
        this.kycDataService = kycDataService;
    }

    public GenerateOTPDto generateOtp(String aadharNumber) {
        aadharNumber = aadharNumber.trim();
        aadharNumber = aadharNumber.replace(" ", "");
        return kycDataVerificationService.generateOTPToGetAadharData(aadharNumber);
    }

    public AadharValidationInfo confirmOtp(String clientId, String otpValue, KYC kyc) {
        String response = kycDataVerificationService.getAadharDataToVerify(clientId, otpValue);
        kycDataService.saveVerificationData(kyc, response);

        try {
            if (!StringUtils.isEmpty(response)) {
                JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(response);
                int statusCode = Integer.valueOf(responseJsonObject.get("status_code").toString());
                if (statusCode == 200) {
                    Object jsonData = responseJsonObject.get("data");
                    if (jsonData != null) {
                        String data = jsonData.toString();
                        AadharVerificationData aadharVerificationData = new ObjectMapper().readValue(data, AadharVerificationData.class);

                        AadharValidationInfo aadharValidationInfo = new AadharValidationInfo();
                        aadharValidationInfo.setNumber(aadharVerificationData.getAadhaar_number());
                        aadharValidationInfo.setName(aadharVerificationData.getFull_name());
                        aadharValidationInfo.setGender(aadharVerificationData.getGender());
                        aadharValidationInfo.setBirthDate(aadharVerificationData.getDob());
                        AadharAddress aadharAddress = new AadharAddress();
                        aadharAddress.setCare_of(aadharVerificationData.getCare_of());
                        aadharAddress.setZip(aadharVerificationData.getZip());
                        aadharAddress.setAddress(aadharVerificationData.getAddress());
                        aadharValidationInfo.setAadharAddress(aadharAddress);
                        return aadharValidationInfo;
                    }
                } else {
                    String errorMessage = responseJsonObject.get("message").toString();
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        } catch (ParseException | JsonProcessingException e) {
        }
        return new AadharValidationInfo();
    }

    public String splitValueAndVerifyData(String value, String valueToMatch) {
        if (value == null)
            return "0";
        List<String> valueList = Arrays.asList(value.split(" "));
        return verifyData(valueList, valueToMatch);
    }

    public String verifyData(List<String> valueList, String valueToMatch) {
        if (valueToMatch != null) {
            Map<String, Integer> valueMatchScoreMap = new HashMap<>();
            valueList.forEach(v -> {
                ExtractedResult extractedResult = FuzzySearch.extractOne(v, Arrays.asList(valueToMatch));
                valueMatchScoreMap.put(v, extractedResult.getScore());
            });
            if (valueMatchScoreMap.size() > 0) {
                int total = 0;
                for (Map.Entry<String, Integer> entry : valueMatchScoreMap.entrySet()) {
                    total = total + entry.getValue();
                }
                double score = total / valueMatchScoreMap.size();
                String result = String.format("%.2f", score);
                return result;
            }
        }
        return "0";
    }


    public String verifyData(String value, String valueToMatch) {
        ExtractedResult extractedResult = FuzzySearch.extractOne(value, Arrays.asList(valueToMatch));
        int score = extractedResult.getScore();
        return String.valueOf(score);
    }

    public String getFaceImageData(KYC kyc) {
        Optional<KYCData> kycData = kycDataService.getKYCData(kyc);
        if (kycData.isPresent()) {
            String verificationData = kycData.get().getVerificationData();
            AadharVerificationData aadharVerificationData = getAadharResponse(verificationData);
            return getFaceImageData(aadharVerificationData);
        }
        throw new RuntimeException("Please capture Aadhar card first.");
    }


    private AadharVerificationData getAadharResponse(String response) {
        try {
            if (!StringUtils.isEmpty(response)) {
                JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(response);
                int statusCode = Integer.valueOf(responseJsonObject.get("status_code").toString());
                if (statusCode == 200) {
                    Object jsonData = responseJsonObject.get("data");
                    if (jsonData != null) {
                        String data = jsonData.toString();
                        AadharVerificationData aadharVerificationData = new ObjectMapper().readValue(data, AadharVerificationData.class);
                        return aadharVerificationData;
                    }
                }
            }
        } catch (Exception e) {
        }
        return new AadharVerificationData();
    }

    private String getFaceImageData(AadharVerificationData aadharVerificationData) {
        if (aadharVerificationData.getProfile_image() != null )
            return aadharVerificationData.getProfile_image();
        return null;
    }

}
