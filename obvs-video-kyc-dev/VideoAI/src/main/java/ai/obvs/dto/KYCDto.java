package ai.obvs.dto;

import ai.obvs.Enums.DocumentName;
import ai.obvs.Enums.KYCType;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.model.KYCResource;
import ai.obvs.dto.workflow.WorkflowStepDto;
public class KYCDto {
    private Long id;
    private Long videoSessionId;
    private KYCType kycType;
    private WorkflowStepDto workflowStepDto;
    private DocumentDto documentDto;
    private KYCVerificationData kycVerificationData;

    public KYCType getKycType() {
        return kycType;
    }

    public void setKycType(KYCType kycType) {
        this.kycType = kycType;
    }

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

    public KYCVerificationData getKycVerificationData() {
        return kycVerificationData;
    }

    public void setKycVerificationData(KYCVerificationData kycVerificationData) {
        this.kycVerificationData = kycVerificationData;
    }
}
