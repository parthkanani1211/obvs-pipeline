package ai.obvs.Enums;

public enum VideoKYCRequestStatus {
    INITIATED ("Initiated"),
    IN_PROGRESS ("In progress"),
    PENDING ("Pending"),
    COMPLETED ("Completed"),
    PENDING_APPROVAL ("Approval Pending"),
    APPROVED ("Approved"),
    REJECTED ("Rejected"),
    CANCELED ("Canceled"),
    TIMEOUT ("Timeout");

    private String auditStatus;

    VideoKYCRequestStatus(String auditStatus){
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus(){
        return this.auditStatus;
    }

    }
