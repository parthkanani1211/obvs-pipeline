package ai.obvs.services;

import ai.obvs.dto.Pan.PANCardData;
import ai.obvs.model.KYC;
import ai.obvs.model.KYCData;
import ai.obvs.repository.KYCDataRepository;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PANCardDataVerificationService {
    private KYCDataService kycDataService;
    private KYCDataVerificationService kycDataVerificationService;

    public PANCardDataVerificationService(KYCDataVerificationService kycDataVerificationService,
                                          KYCDataService kycDataService) {
        this.kycDataVerificationService = kycDataVerificationService;
        this.kycDataService = kycDataService;
    }

    public PANCardData getDataToVerify(KYC kyc, String panNumber) {

        String response = kycDataVerificationService.getPanDataToVerify(panNumber);
        kycDataService.saveVerificationData(kyc, response);

        String fullName = getFullName(response);
        PANCardData panCardData = new PANCardData();
        panCardData.setNumber(panNumber);
        panCardData.setName(fullName);
        panCardData.setParentName(fullName);
        panCardData.setBirthDate("");
        return panCardData;
    }

    public String verifyData(String value, String valueToMatch) {
        List<String> valueList = Arrays.asList(value.split(" "));
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
        return "0";
    }

    private String getFullName(String response) {
        try {
            if (!StringUtils.isEmpty(response)) {
                JSONObject responseJsonObject = (JSONObject) new JSONParser().parse(response);
                int statusCode = Integer.valueOf(responseJsonObject.get("status_code").toString());
                if (statusCode == 200) {
                    JSONObject data = (JSONObject) responseJsonObject.get("data");
                    if (data != null) {
                        String full_name = data.get("full_name").toString();
                        return full_name;
                    }
                }
            }
        } catch (ParseException e) {
        }
        throw new IllegalArgumentException();
    }

}
