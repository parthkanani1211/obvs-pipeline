package ai.obvs.dto;

import ai.obvs.model.Image;

import java.util.HashMap;
import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.workflow.DocumentDto;

import java.util.List;
import java.util.Map;

public class KYCVerificationData {
    private DocumentName documentName;
    private List<KYCDataMatchResult> matchResults;
    private List<Image> imageList;
    private boolean isVerified;

    public DocumentName getDocumentName() {
        return documentName;
    }

    public void setDocumentName(DocumentName documentName) {
        this.documentName = documentName;
    }

    public List<KYCDataMatchResult> getMatchResults() {
        return matchResults;
    }

    public void setMatchResults(List<KYCDataMatchResult> matchResults) {
        this.matchResults = matchResults;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
