package ai.obvs.services.ocr.dto;

import java.util.List;

public class Result {
    private String fileName;
    private String resultFolderPath;
    private List<ImageFileResult> imageFileResults;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getResultFolderPath() {
        return resultFolderPath;
    }

    public void setResultFolderPath(String resultFolderPath) {
        this.resultFolderPath = resultFolderPath;
    }

    public List<ImageFileResult> getImageFileResults() {
        return imageFileResults;
    }

    public void setImageFileResults(List<ImageFileResult> imageFileResults) {
        this.imageFileResults = imageFileResults;
    }
}
