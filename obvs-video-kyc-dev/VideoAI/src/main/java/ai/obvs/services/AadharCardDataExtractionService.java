package ai.obvs.services;

import ai.obvs.dto.InputImageDto;
import ai.obvs.dto.Pan.PANCardData;
import ai.obvs.integration.models.Aadhar;
import ai.obvs.integration.models.AadharResponse;
import ai.obvs.integration.models.PanResponse;
import ai.obvs.model.AadharInfo;
import ai.obvs.model.KYC;
import ai.obvs.model.KYCData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class AadharCardDataExtractionService {

    private AadharInfo aadharInfo = new AadharInfo();
    private String faceImageData;

    private KYCDataExtractionService kycDataExtractionService;
    private KYCDataService kycDataService;
    private ObjectMapper objectMapper;
    private static final String AADHAR_EXTRACTION_URL = "/inference/aadhar/";

    public AadharCardDataExtractionService(KYCDataExtractionService kycDataExtractionService,
                                           KYCDataService kycDataService) {
        this.kycDataExtractionService = kycDataExtractionService;
        this.kycDataService = kycDataService;
        objectMapper = new ObjectMapper();
    }

    private AadharResponse extractData(String frontImageFilePath, String backImageFilePath) {
        AadharResponse aadharResponse = null;

        try {
            String frontImageExtractedData = kycDataExtractionService.getExtractedData(AADHAR_EXTRACTION_URL, frontImageFilePath);
            String backImageExtractedData = kycDataExtractionService.getExtractedData(AADHAR_EXTRACTION_URL, backImageFilePath);

            AadharResponse frontAadharResponse = new ObjectMapper().readValue(frontImageExtractedData, AadharResponse.class);
            AadharResponse backAadharResponse = new ObjectMapper().readValue(backImageExtractedData, AadharResponse.class);

            if (backAadharResponse.getAadhar().getName() == null && backAadharResponse.getAadhar().getGender() == null && backAadharResponse.getAadhar().getBirthDate() == null) {
                frontAadharResponse.getAadhar().setAddress(backAadharResponse.getAadhar().getAddress());
                aadharResponse = frontAadharResponse;
            } else if (frontAadharResponse.getAadhar().getName() == null && frontAadharResponse.getAadhar().getGender() == null && frontAadharResponse.getAadhar().getBirthDate() == null) {
                backAadharResponse.getAadhar().setAddress(frontAadharResponse.getAadhar().getAddress());
                aadharResponse = backAadharResponse;
            }

            if (aadharResponse == null)
                aadharResponse = frontAadharResponse;


        } catch (JsonProcessingException e) {
            int i = 0;
        } catch (Exception e) {
            int i = 0;
        }
        return aadharResponse;
    }

    public AadharInfo extractData(KYC kyc, String frontImageData, String backImageData) {
        AadharResponse aadharResponse = extractData(frontImageData, backImageData);
        validateData(aadharResponse);
        try {
            String value = objectMapper.writeValueAsString(aadharResponse);
            kycDataService.saveExtractedData(kyc, value);
        } catch (JsonProcessingException e) {
        }

        return aadharInfo;
    }

    private void validateData(AadharResponse aadharResponse) {
        AadharInfo aadharInfo = new AadharInfo();
        if (aadharResponse.getAadhar() != null) {
            if (aadharResponse.getAadhar().getId() != null)
                aadharInfo.setNumber(aadharResponse.getAadhar().getId().getValue());
            if (aadharResponse.getAadhar().getName() != null)
                aadharInfo.setName(aadharResponse.getAadhar().getName().getValue());
            if (aadharResponse.getAadhar().getBirthDate() != null)
                aadharInfo.setBirthDate(aadharResponse.getAadhar().getBirthDate().getValue());
            if (aadharResponse.getAadhar().getGender() != null)
                aadharInfo.setGender(aadharResponse.getAadhar().getGender().getValue());
            if (aadharResponse.getAadhar().getAddress() != null)
                aadharInfo.setAddress(aadharResponse.getAadhar().getAddress().getValue());
            if (aadharResponse.getAadhar().getPhoto() != null)
                aadharInfo.setFaceImageData(aadharResponse.getAadhar().getPhoto().getValue());
        }
        this.aadharInfo = aadharInfo;
    }

    public AadharInfo getAadharInfo() {
        return aadharInfo;
    }

    public void setAadharInfo(AadharInfo aadharInfo) {
        this.aadharInfo = aadharInfo;
    }

    public String getFaceImageData() {
        return faceImageData;
    }

    public void setFaceImageData(String faceImageData) {
        this.faceImageData = faceImageData;
    }

    public String getFaceImageData(KYC kyc) {
        Optional<KYCData> kycData = kycDataService.getKYCData(kyc);
        if (kycData.isPresent()) {
            String extractedData = kycData.get().getExtractedData();
            AadharResponse aadharResponse = getAadharResponse(extractedData);
            return getFaceImageData(aadharResponse);
        }
        throw new RuntimeException("Please capture Aadhar card first.");
    }

    private AadharResponse getAadharResponse(String extractedData) {
        AadharResponse aadharResponse = new AadharResponse();
        try {
            aadharResponse = new ObjectMapper().readValue(extractedData, AadharResponse.class);
        } catch (JsonProcessingException e) {
        }
        return aadharResponse;
    }

    private String getFaceImageData(AadharResponse aadharResponse) {
        if (aadharResponse.getAadhar() != null && aadharResponse.getAadhar().getPhoto() != null)
            return aadharResponse.getAadhar().getPhoto().getValue();
        return null;
    }
}
