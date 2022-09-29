package ai.obvs.services;

import ai.obvs.dto.face.FaceVerificationResponse;

import java.util.Map;

public interface BioIdVerificationService {
    FaceVerificationResponse verifyPhoto(String capturedImageData1, String capturedImageData2, String imageToCompare);
}
