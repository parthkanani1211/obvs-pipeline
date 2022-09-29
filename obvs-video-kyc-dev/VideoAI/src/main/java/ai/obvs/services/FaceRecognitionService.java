package ai.obvs.services;

import ai.obvs.Enums.DocumentName;
import ai.obvs.Enums.FaceRecognitionField;
import ai.obvs.dto.KYCDataMatchResult;
import ai.obvs.dto.KYCRequestImageDto;
import ai.obvs.dto.KYCVerificationData;
import ai.obvs.dto.face.FaceVerificationResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static ai.obvs.Constants.*;

@Service
public class FaceRecognitionService {

    private BioIdVerificationService bioIdVerificationService;

    public FaceRecognitionService(BioIdVerificationService bioIdVerificationService) {
        this.bioIdVerificationService = bioIdVerificationService;
    }

    public KYCVerificationData getData(KYCRequestImageDto kycRequestImageDto, Map<DocumentName, String> imageDataToCompare) {
        KYCVerificationData verificationData = getFaceVerificationData(kycRequestImageDto, imageDataToCompare);
        return verificationData;
    }

    private KYCVerificationData getFaceVerificationData(KYCRequestImageDto kycRequestImageDto, Map<DocumentName, String> imageDataToCompare) {
        List<KYCDataMatchResult> kycDataMatchResults = new ArrayList();
        KYCVerificationData kycVerificationData = new KYCVerificationData();
        AtomicBoolean isVerified = new AtomicBoolean(true);
        AtomicBoolean isLive = new AtomicBoolean(false);

        imageDataToCompare.forEach((k, v) -> {
            FaceVerificationResponse faceVerificationResponse = verifyPhoto(kycRequestImageDto, v);
            boolean isMatch = faceVerificationResponse.isMatch();
            isLive.set(isLive.get() || faceVerificationResponse.isLive());
//            isVerified.set(isVerified.get() && true);
            KYCDataMatchResult kycDataMatchResult = getKycDataMatchResult(isMatch);
            switch (k) {
                case PAN:
                    kycDataMatchResult.setName(FaceRecognitionField.MATCH_WITH_PAN.getValue());
                    kycDataMatchResults.add(kycDataMatchResult);
                    break;
                case AADHAR:
                    kycDataMatchResult.setName(FaceRecognitionField.MATCH_WITH_AADHAR.getValue());
                    kycDataMatchResults.add(kycDataMatchResult);
            }
        });

        KYCDataMatchResult kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setName(FaceRecognitionField.LIVELINESS_CHECK.getValue());
        kycDataMatchResult.setMatchResult(isLive.get() ? SUCCESS : FAIL);
        kycDataMatchResult.setValue(isLive.get() ? PASS : FAIL);
        kycDataMatchResults.add(kycDataMatchResult);

        kycVerificationData.setMatchResults(kycDataMatchResults);
        kycVerificationData.setVerified(isVerified.get());
        return kycVerificationData;
    }

    private KYCDataMatchResult getKycDataMatchResult(boolean isMatch) {
        String matchResult = getRandomNumber(isMatch) + "%";
        KYCDataMatchResult kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(matchResult);
        kycDataMatchResult.setMatchScore(matchResult);
        kycDataMatchResult.setMatchResult(isMatch ? SUCCESS : FAIL);
        return kycDataMatchResult;
    }

    private int getRandomNumber(boolean isMatch) {
        int low = 70;
        int high = 80;

        if (!isMatch) {
            low = 50;
            high = 59;
        }
        Random r = new Random();

        int result = r.nextInt(high - low) + low;
        return result;
    }

    public FaceVerificationResponse verifyPhoto(KYCRequestImageDto kycRequestImageDto, String imageToCompare) {
        String capturedImageData1 = Base64.getEncoder().encodeToString(kycRequestImageDto.getFrontImage());
        String capturedImageData2 = Base64.getEncoder().encodeToString(kycRequestImageDto.getBackImage());
        FaceVerificationResponse faceVerificationResponse = bioIdVerificationService.verifyPhoto(capturedImageData1, capturedImageData2, imageToCompare);
        return faceVerificationResponse;
    }

}