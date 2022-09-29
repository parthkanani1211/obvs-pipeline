package ai.obvs.dto;

import ai.obvs.Enums.KYCType;

public class WorkflowStepDto {
    private int orderNo;
    private KYCType kycType;
    private int inputImages;

    public KYCType getKycType() {
        return kycType;
    }

    public void setKycType(KYCType kycType) {
        this.kycType = kycType;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getInputImages() {
        return inputImages;
    }

    public void setInputImages(int inputImages) {
        this.inputImages = inputImages;
    }
}
