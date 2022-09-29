package ai.obvs.services;

import ai.obvs.Enums.AadharField;
import ai.obvs.dto.Aadhar.AadharValidationInfo;
import ai.obvs.dto.Aadhar.AadharDataToVerify;
import ai.obvs.dto.Aadhar.GenerateOTPDto;
import ai.obvs.dto.KYCRequestImageDto;
import ai.obvs.model.AadharInfo;
import ai.obvs.dto.KYCDataMatchResult;
import ai.obvs.dto.KYCVerificationData;
import ai.obvs.model.KYC;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static ai.obvs.Constants.FAIL;
import static ai.obvs.Constants.SUCCESS;

public class AadharCardService {

    private AadharCardDataVerificationService aadharCardDataVerificationService;
    private AadharCardDataExtractionService aadharCardDataExtractionService;
    private AadharVerificationCachingService aadharVerificationCachingService;


    public AadharCardService(AadharCardDataExtractionService aadharCardDataExtractionService,
                             AadharCardDataVerificationService aadharCardDataVerificationService,
                             AadharVerificationCachingService aadharVerificationCachingService) {

        this.aadharCardDataExtractionService = aadharCardDataExtractionService;
        this.aadharCardDataVerificationService = aadharCardDataVerificationService;
        this.aadharVerificationCachingService = aadharVerificationCachingService;
    }

    public String getPhotoImage(KYC kyc) {
        return aadharCardDataExtractionService.getFaceImageData(kyc);
    }

    public String getVerificationPhotoImage(KYC kyc) {
        return aadharCardDataVerificationService.getFaceImageData(kyc);
    }

    public KYCVerificationData getData(KYC kyc, KYCRequestImageDto kycRequestImageDto) {
        aadharCardDataExtractionService.extractData(kyc, kycRequestImageDto.getFrontImagePath(), kycRequestImageDto.getBackImagePath());

        AadharInfo aadharInfo = aadharCardDataExtractionService.getAadharInfo();
        KYCVerificationData verificationData = getExtractedResult(aadharInfo);
        return verificationData;
    }

    public GenerateOTPDto generateOtp(Long requestId, KYCVerificationData kycVerificationData) {
        List<KYCDataMatchResult> matchResults = kycVerificationData.getMatchResults();
        Optional<KYCDataMatchResult> kycDataMatchResultOptional = matchResults.stream()
                .filter(matchResult -> matchResult.getName().equals(AadharField.NUMBER.getValue())).findAny();
        GenerateOTPDto generateOTPDto = aadharCardDataVerificationService.generateOtp(kycDataMatchResultOptional.get().getValue().trim());

        aadharVerificationCachingService.remove(requestId);
        AadharDataToVerify aadharDataToVerify = new AadharDataToVerify();
        aadharDataToVerify.setKycVerificationData(kycVerificationData);
        aadharDataToVerify.setClientId(generateOTPDto.getClientId());
        aadharVerificationCachingService.put(requestId, aadharDataToVerify);
        return generateOTPDto;
    }

    public void validateOtp(Long requestId, String otpValue, KYC kyc) {
        AadharDataToVerify aadharDataToVerify = aadharVerificationCachingService.get(requestId);
        aadharDataToVerify.setOtpValue(otpValue);
        AadharValidationInfo aadharValidationInfo = aadharCardDataVerificationService.confirmOtp(aadharDataToVerify.getClientId(), otpValue, kyc);
        aadharDataToVerify.setAadharValidationInfo(aadharValidationInfo);
    }

    public KYCVerificationData verifyData(Long requestId) {
        AadharDataToVerify aadharDataToVerify = aadharVerificationCachingService.get(requestId);
        return matchValue(aadharDataToVerify.getKycVerificationData(), aadharDataToVerify.getAadharValidationInfo());
    }

    private KYCVerificationData getExtractedResult(AadharInfo aadharInfo) {
        List<KYCDataMatchResult> kycDataMatchResults = new ArrayList<>();
        KYCVerificationData kycVerificationData = new KYCVerificationData();
        boolean isVerified = true;

        KYCDataMatchResult kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(aadharInfo.getNumber());
        kycDataMatchResult.setExtractedValue(aadharInfo.getNumber());
        boolean isNumberEmpty = StringUtils.isEmpty(aadharInfo.getNumber());
        kycDataMatchResult.setMatchResult(isNumberEmpty ? FAIL : SUCCESS);
        kycDataMatchResult.setMatchResult(SUCCESS);
        isVerified = isVerified && !isNumberEmpty;
        kycDataMatchResult.setName(AadharField.NUMBER.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(aadharInfo.getName());
        kycDataMatchResult.setExtractedValue(aadharInfo.getName());
        boolean isNameVerified = StringUtils.isEmpty(aadharInfo.getName());
        kycDataMatchResult.setMatchResult(isNameVerified ? FAIL : SUCCESS);
        kycDataMatchResult.setMatchResult(SUCCESS);

        isVerified = isVerified && !isNameVerified;
        kycDataMatchResult.setName(AadharField.NAME.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(aadharInfo.getGender());
        kycDataMatchResult.setExtractedValue(aadharInfo.getGender());
        kycDataMatchResult.setName(AadharField.GENDER.getValue());
        kycDataMatchResult.setMatchResult(SUCCESS);

        kycDataMatchResults.add(kycDataMatchResult);

        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(aadharInfo.getBirthDate());
        kycDataMatchResult.setExtractedValue(aadharInfo.getBirthDate());
        boolean isBirthDateVerified = StringUtils.isEmpty(aadharInfo.getBirthDate());
        kycDataMatchResult.setMatchResult(isBirthDateVerified ? FAIL : SUCCESS);
        kycDataMatchResult.setMatchResult(SUCCESS);

        isVerified = isVerified && !isBirthDateVerified;
        kycDataMatchResult.setName(AadharField.DOB.getValue());
        kycDataMatchResults.add(kycDataMatchResult);
        kycDataMatchResult.setMatchResult(SUCCESS);


        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(aadharInfo.getAddress());
        kycDataMatchResult.setExtractedValue(aadharInfo.getAddress());
        boolean isAddressVerified = StringUtils.isEmpty(aadharInfo.getAddress());
        kycDataMatchResult.setMatchResult(isAddressVerified ? FAIL : SUCCESS);
        kycDataMatchResult.setMatchResult(SUCCESS);

        isVerified = isVerified && !isAddressVerified;
        kycDataMatchResult.setName(AadharField.ADDRESS.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycVerificationData.setMatchResults(kycDataMatchResults);
//        kycVerificationData.setVerified(isVerified);
        return kycVerificationData;
    }

    private KYCVerificationData matchValue(KYCVerificationData kycVerificationData, AadharValidationInfo aadharValidationInfo) {

        List<KYCDataMatchResult> matchResults = kycVerificationData.getMatchResults();
        if (aadharValidationInfo == null) {
            throw new IllegalArgumentException("Unable to verify. Please try again");
        }
        AtomicBoolean isVerified = new AtomicBoolean(true);
        matchResults.forEach(matchResult -> {
            String result = "NA";
            AadharField aadharField = AadharField.EnumOfString(matchResult.getName());
            switch (aadharField) {
                case NUMBER:
                    matchResult.setMatchValue("NA");
                    matchResult.setMatchResult("NA");
                    break;
                case NAME:
                    matchResult.setMatchValue(aadharValidationInfo.getName());
                    result = aadharCardDataVerificationService.splitValueAndVerifyData(matchResult.getValue(), matchResult.getMatchValue());
                    boolean isMatch = verifyAndSetMatchResult(matchResult, result);
                    isVerified.set(isVerified.get() && isMatch);
                    break;
                case GENDER:
                    matchResult.setMatchValue(aadharValidationInfo.getGender());
                    result = aadharCardDataVerificationService.splitValueAndVerifyData(matchResult.getValue(), matchResult.getMatchValue());
                    isMatch = verifyAndSetMatchResult(matchResult, result);
                    isVerified.set(isVerified.get() && isMatch);
                    //isVerified.set(isVerified.get() && verifyAndSetMatchResult(matchResult, result));
                    break;
                case DOB:
                    matchResult.setMatchValue(aadharValidationInfo.getBirthDate());
                    result = aadharCardDataVerificationService.splitValueAndVerifyData(matchResult.getValue(), matchResult.getMatchValue());
                    isMatch = verifyAndSetMatchResult(matchResult, result);
                    isVerified.set(isVerified.get() && isMatch);
//                    isVerified.set(isVerified.get() && verifyAndSetMatchResult(matchResult, result));
                    break;
                case ADDRESS:
                    if (aadharValidationInfo.getAadharAddress() != null)
                        matchResult.setMatchValue(aadharValidationInfo.getAadharAddress().toString());
                    result = aadharCardDataVerificationService.verifyData(matchResult.getValue(), matchResult.getMatchValue());
                    isMatch = verifyAndSetMatchResult(matchResult, result);
                    isVerified.set(isVerified.get() && isMatch);
//                    isVerified.set(isVerified.get() && verifyAndSetMatchResult(matchResult, result));
                    break;
            }
        });

        kycVerificationData.setMatchResults(matchResults);
        kycVerificationData.setVerified(isVerified.get());
        return kycVerificationData;
    }

    private boolean verifyAndSetMatchResult(KYCDataMatchResult matchResult, String result) {
        matchResult.setMatchScore(result);
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(result));
        int intValue = bigDecimal.intValue();
        boolean isVerified = intValue > 70;
        matchResult.setMatchResult(isVerified ? SUCCESS : FAIL);
        return isVerified;
    }
}

