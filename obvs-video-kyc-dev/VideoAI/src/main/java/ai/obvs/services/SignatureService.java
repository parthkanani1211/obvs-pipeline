package ai.obvs.services;

import ai.obvs.Enums.FaceRecognitionField;
import ai.obvs.Enums.SignatureField;
import ai.obvs.dto.KYCDataMatchResult;
import ai.obvs.dto.KYCRequestImageDto;
import ai.obvs.dto.KYCVerificationData;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.*;

import static ai.obvs.Constants.SUCCESS;

@Service
public class SignatureService {

    public KYCVerificationData getData(KYCRequestImageDto kycRequestImageDto) {
        String imageFilePath = kycRequestImageDto.getFrontImagePath();
        String fileName = Paths.get(imageFilePath).getFileName().toString();
        KYCVerificationData verificationData = getSignVerificationData();
        return verificationData;
    }

    private KYCVerificationData getSignVerificationData() {

        List<KYCDataMatchResult> kycDataMatchResults = new ArrayList();
        KYCVerificationData kycVerificationData = new KYCVerificationData();
        boolean isVerified = true;

        KYCDataMatchResult kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(getRandomNumber() + "%");
        kycDataMatchResult.setMatchResult(SUCCESS);
        kycDataMatchResult.setName(SignatureField.IS_VALID_SIGN.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycDataMatchResult = new KYCDataMatchResult();
        kycDataMatchResult.setValue(getRandomNumber() + "%");
        kycDataMatchResult.setMatchResult(SUCCESS);
        kycDataMatchResult.setName(SignatureField.MATCH_WITH_PAN.getValue());
        kycDataMatchResults.add(kycDataMatchResult);

        kycVerificationData.setMatchResults(kycDataMatchResults);
        kycVerificationData.setVerified(isVerified);
        return kycVerificationData;
    }

    private int getRandomNumber() {
        Random r = new Random();
        int low = 70;
        int high = 79;
        int result = r.nextInt(high - low) + low;
        return result;
    }
}