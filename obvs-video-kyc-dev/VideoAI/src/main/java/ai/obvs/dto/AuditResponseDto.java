package ai.obvs.dto;

public class AuditResponseDto {
    private Long auditId;
    private Long videoKYCId;

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getVideoKYCId() {
        return videoKYCId;
    }

    public void setVideoKYCId(Long videoKYCId) {
        this.videoKYCId = videoKYCId;
    }
}
