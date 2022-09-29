package ai.obvs.services;

public interface KYCDataExtractionService {
    String getExtractedData(String urlSuffix, String imageFilePath);
}
