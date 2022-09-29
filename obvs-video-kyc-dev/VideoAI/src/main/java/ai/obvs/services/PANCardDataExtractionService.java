package ai.obvs.services;

import ai.obvs.integration.models.PanResponse;
import ai.obvs.dto.Pan.PANCardData;
import ai.obvs.model.KYC;
import ai.obvs.model.KYCData;
import ai.obvs.repository.KYCDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class PANCardDataExtractionService {
    private static final String PAN_EXTRACTION_URL = "/inference/pan/";

    private KYCDataExtractionService kycDataExtractionService;
    private KYCDataService kycDataService;
    private PANCardData panCardData = new PANCardData();
    private String signatureImageData;
    private String faceImageData;


    public PANCardDataExtractionService(KYCDataExtractionService kycDataExtractionService, KYCDataService kycDataService){
        this.kycDataExtractionService = kycDataExtractionService;
        this.kycDataService = kycDataService;
    }

    public PANCardData extractData(KYC kyc, String imageFilePath) {
        String extractedData = kycDataExtractionService.getExtractedData(PAN_EXTRACTION_URL, imageFilePath);
        kycDataService.saveExtractedData(kyc, extractedData);
        PanResponse panResponse = getPanResponse(extractedData);
        validatePanData(panResponse);
        return panCardData;
    }

    private PanResponse getPanResponse(String extractedData) {
        PanResponse panResponse = new PanResponse();
        try {
            panResponse = new ObjectMapper().readValue(extractedData, PanResponse.class);
        } catch (JsonProcessingException e) {
        }
        return panResponse;
    }

    private void validatePanData(PanResponse panResponse) {
        PANCardData panCardData = new PANCardData();
        if (panResponse.getPan() != null) {
            if (panResponse.getPan().getId() != null)
                panCardData.setNumber(panResponse.getPan().getId().getValue());
            if (panResponse.getPan().getAn() != null)
                panCardData.setName(panResponse.getPan().getAn().getValue());
            if (panResponse.getPan().getRn() != null)
                panCardData.setParentName(panResponse.getPan().getRn().getValue());
            if (panResponse.getPan().getBirthDate() != null)
                panCardData.setBirthDate(panResponse.getPan().getBirthDate().getValue());
            this.panCardData = panCardData;

            if (panResponse.getPan().getSignature() != null)
                this.signatureImageData = panResponse.getPan().getSignature().getValue();
            if (panResponse.getPan().getPhoto() != null)
                this.faceImageData = panResponse.getPan().getPhoto().getValue();
        }
    }

    public PANCardData getPanCardData() {
        return panCardData;
    }

    public String getSignatureImageData() {
        return signatureImageData;
    }

    public String getFaceImageData() {
        return faceImageData;
    }

    public String getFaceImageData(KYC kyc) {
        Optional<KYCData> kycData = kycDataService.getKYCData(kyc);
        if (kycData.isPresent()) {
            String extractedData = kycData.get().getExtractedData();
            PanResponse panResponse = getPanResponse(extractedData);
            return getFaceImageData(panResponse);
        }
        throw new RuntimeException("Please capture PAN card first.");
    }

    private String getFaceImageData(PanResponse panResponse) {
        if (panResponse.getPan() != null && panResponse.getPan().getPhoto() != null)
            return panResponse.getPan().getPhoto().getValue();
        return null;
    }
}
