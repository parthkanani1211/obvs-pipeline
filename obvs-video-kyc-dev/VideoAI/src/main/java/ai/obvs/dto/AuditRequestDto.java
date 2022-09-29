package ai.obvs.dto;

public class AuditRequestDto {
    private Long customerId;
    private Long agentId;
    private Long videoKYCId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getVideoKYCId() {
        return videoKYCId;
    }

    public void setVideoKYCId(Long videoKYCId) {
        this.videoKYCId = videoKYCId;
    }
}
