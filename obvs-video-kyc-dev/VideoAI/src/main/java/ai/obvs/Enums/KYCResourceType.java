package ai.obvs.Enums;

public enum KYCResourceType {
    PAN("Pan"),
    AADHAR_FRONT("Aadhaar Front"),
    AADHAR_BACK("Aadhaar Back"),
    FACE("Face"),
    SIGN("Sign"),
    PAN_SIGN("Pan Sign"),
    PAN_FACE("Pan Face"),
    AADHAR_FACE("Aadhaar Face");


    private String value;

    KYCResourceType(String roleValue) {
        this.value = roleValue;
    }

    public String getValue() {
        return value;
    }
}
