package ai.obvs.dto.Aadhar;

import ai.obvs.model.AadharInfo;

public class AadharValidationInfo extends AadharInfo {
    private AadharAddress aadharAddress;

    public AadharAddress getAadharAddress() {
        return aadharAddress;
    }

    public void setAadharAddress(AadharAddress aadharAddress) {
        this.aadharAddress = aadharAddress;
    }
}
