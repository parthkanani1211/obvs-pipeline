package ai.obvs.services.ocr.dto;

public class ImageFileResult {
    private String fileName;
    private Classification classification;
    private ExtractData data;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public ExtractData getData() {
        return data;
    }

    public void setData(ExtractData data) {
        this.data = data;
    }
}
