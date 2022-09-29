package ai.obvs.dto;


import ai.obvs.Enums.VideoKYCRequestStatus;

import java.util.Map;

public class AuditDataDto {
    private CustomerInfo customerInfo;
    private AgentDto agentDto;
    private Map<String,KYCVerificationData> kycTypeKYCVerificationDataMap;
    private VideoKYCRequestStatus status;
    private String comment;

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public AgentDto getAgentDto() {
        return agentDto;
    }

    public void setAgentDto(AgentDto agentDto) {
        this.agentDto = agentDto;
    }

    public Map<String, KYCVerificationData> getKycTypeKYCVerificationDataMap() {
        return kycTypeKYCVerificationDataMap;
    }

    public void setKycTypeKYCVerificationDataMap(Map<String, KYCVerificationData> kycTypeKYCVerificationDataMap) {
        this.kycTypeKYCVerificationDataMap = kycTypeKYCVerificationDataMap;
    }

    public VideoKYCRequestStatus getStatus() {
        return status;
    }

    public void setStatus(VideoKYCRequestStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
