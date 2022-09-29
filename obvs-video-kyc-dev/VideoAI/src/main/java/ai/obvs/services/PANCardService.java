package ai.obvs.services;

import ai.obvs.Enums.PanField;
import ai.obvs.dto.InputImageDto;
import ai.obvs.dto.KYCRequestImageDto;
import ai.obvs.dto.Pan.PANCardData;
import ai.obvs.dto.KYCDataMatchResult;
import ai.obvs.dto.KYCVerificationData;
import ai.obvs.model.KYC;
import ai.obvs.model.KYCData;
import ai.obvs.repository.KYCDataRepository;
import liquibase.pro.packaged.K;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static ai.obvs.Constants.FAIL;
import static ai.obvs.Constants.SUCCESS;

public class PANCardService {
    private PANCardDataVerificationService panCardDataVerificationService;
    private PANCardDataExtractionService panCardDataExtractionService;


    public PANCardService(PANCardDataExtractionService panCardDataExtractionService, PANCardDataVerificationService panCardDataVerificationService) {
        this.panCardDataExtractionService = panCardDataExtractionService;
        this.panCardDataVerificationService = panCardDataVerificationService;
    }

    public KYCVerificationData getData(KYC kyc, KYCRequestImageDto kycRequestImageDto) {
        String imageFilePath = kycRequestImageDto.getFrontImagePath();
        PANCardData panCardData = panCardDataExtractionService.extractData(kyc, imageFilePath);
        KYCVerificationData panCardVerificationData = getExtractedResult(panCardData);
        return panCardVerificationData;
    }

    public KYCVerificationData verifyData(KYC kyc, KYCVerificationData kycVerificationData) {
        List<KYCDataMatchResult> matchResults = kycVerificationData.getMatchResults();
        PANCardData dataToVerify = new PANCardData();
        if (matchResults != null) {
            Optional<KYCDataMatchResult> matchResultOptional = matchResults.stream().filter(matchResult -> matchResult.getName().equals(PanField.NUMBER.getValue())).findAny();
            try {
                dataToVerify = panCardDataVerificationService.getDataToVerify(kyc, matchResultOptional.get().getValue().trim());
            } catch (IllegalArgumentException e) {
            }
        }
        return matchValue(kycVerificationData, dataToVerify);
    }

    public String getPhotoImage(KYC kyc) {
        return panCardDataExtractionService.getFaceImageData(kyc);
    }

    private KYCVerificationData getExtractedResult(PANCardData panCardData) {
        List<KYCDataMatchResult> kycDataMatchResults = new ArrayList<>();
        KYCVerificationData kycVerificationData = new KYCVerificationData();
        boolean isVerified = true;

        KYCDataMatchResult kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(panCardData.getNumber());
        kycDataMatchResult.setExtractedValue(panCardData.getNumber());
        boolean isNumberEmpty = StringUtils.isEmpty(panCardData.getNumber());
        kycDataMatchResult.setMatchResult(isNumberEmpty ? FAIL : SUCCESS);
        isVerified = isVerified && !isNumberEmpty;
        kycDataMatchResult.setName(PanField.NUMBER.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(panCardData.getName());
        kycDataMatchResult.setExtractedValue(panCardData.getName());
        boolean isNameVerified = StringUtils.isEmpty(panCardData.getName());
        kycDataMatchResult.setMatchResult(isNameVerified ? FAIL : SUCCESS);
        isVerified = isVerified && !isNameVerified;
        kycDataMatchResult.setName(PanField.NAME.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(panCardData.getParentName());
        kycDataMatchResult.setExtractedValue(panCardData.getParentName());
        boolean isParentNameVerified = StringUtils.isEmpty(panCardData.getParentName());
        kycDataMatchResult.setMatchResult(isParentNameVerified ? FAIL : SUCCESS);
        isVerified = isVerified && !isParentNameVerified;
        kycDataMatchResult.setName(PanField.PARENT_NAME.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(panCardData.getBirthDate());
        kycDataMatchResult.setExtractedValue(panCardData.getBirthDate());
        boolean isBirthDateVerified = StringUtils.isEmpty(panCardData.getBirthDate());
        kycDataMatchResult.setMatchResult(isBirthDateVerified ? FAIL : SUCCESS);
        isVerified = isVerified && !isBirthDateVerified;
        kycDataMatchResult.setName(PanField.DOB.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycVerificationData.setMatchResults(kycDataMatchResults);
//        kycVerificationData.setVerified(isVerified);
        return kycVerificationData;
    }

    private KYCVerificationData matchValue(KYCVerificationData kycVerificationData, PANCardData panCardData) {

        List<KYCDataMatchResult> matchResults = kycVerificationData.getMatchResults();
        if (matchResults != null) {
            AtomicBoolean isVerified = new AtomicBoolean(true);

            matchResults.forEach(matchResult -> {
                String result = "NA";
                PanField panField = PanField.EnumOfString(matchResult.getName());
                switch (panField) {
                    case NUMBER:
                        matchResult.setMatchValue(panCardData.getNumber());
                        result = panCardDataVerificationService.verifyData(matchResult.getValue(), matchResult.getMatchValue());
                        boolean isMatch = verifyAndSetMatchResult(matchResult, result);
                        isVerified.set(isVerified.get() && isMatch);
                        break;
                    case DOB:
                    case PARENT_NAME:
                        matchResult.setMatchValue("NA");
                        matchResult.setMatchResult(result);
                        break;
                    case NAME:
                        matchResult.setMatchValue(panCardData.getName());
                        result = panCardDataVerificationService.verifyData(matchResult.getValue(), matchResult.getMatchValue());
                        isMatch = verifyAndSetMatchResult(matchResult, result);
                        isVerified.set(isVerified.get() && isMatch);
                        break;
//                    matchResult.setMatchValue(panCardData.getParentName());
//                    result = panCardDataVerificationService.verifyData(matchResult.getValue(), matchResult.getMatchValue());
//                    isVerified.set(isVerified.get() && verifyAndSetMatchResult(matchResult, result));
//                    break;
                }
            });

            kycVerificationData.setMatchResults(matchResults);
            kycVerificationData.setVerified(isVerified.get());
        }
        return kycVerificationData;
    }

    private boolean verifyAndSetMatchResult(KYCDataMatchResult v, String result) {
        v.setMatchScore(result);
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(result));
        int intValue = bigDecimal.intValue();
        boolean isVerified = intValue > 70;
        v.setMatchResult(isVerified ? SUCCESS : FAIL);
        return isVerified;
    }
}
