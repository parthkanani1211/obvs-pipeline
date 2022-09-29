package ai.obvs.dto;

import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.model.KYCResource;

import javax.print.attribute.standard.DocumentName;
import java.util.Set;

public class KYCResourcesDto {
    private Long id;
    private Long videoSessionId;
    private WorkflowStepDto workflowStepDto;
    private DocumentDto documentDto;
    private String extractedData;
    private String KYCVerificationData;
    private Set<KYCResource> resources;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVideoSessionId() {
        return videoSessionId;
    }

    public void setVideoSessionId(Long videoSessionId) {
        this.videoSessionId = videoSessionId;
    }

    public WorkflowStepDto getWorkflowStepDto() {
        return workflowStepDto;
    }

    public void setWorkflowStepDto(WorkflowStepDto workflowStepDto) {
        this.workflowStepDto = workflowStepDto;
    }

    public DocumentDto getDocumentDto() {
        return documentDto;
    }

    public void setDocumentDto(DocumentDto documentDto) {
        this.documentDto = documentDto;
    }

    public String getExtractedData() {
        return extractedData;
    }

    public void setExtractedData(String extractedData) {
        this.extractedData = extractedData;
    }

    public String getKYCVerificationData() {
        return KYCVerificationData;
    }

    public void setKYCVerificationData(String KYCVerificationData) {
        this.KYCVerificationData = KYCVerificationData;
    }

    public Set<KYCResource> getResources() {
        return resources;
    }

    public void setResources(Set<KYCResource> resources) {
        this.resources = resources;
    }
}
