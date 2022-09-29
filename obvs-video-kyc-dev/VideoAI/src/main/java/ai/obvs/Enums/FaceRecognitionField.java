package ai.obvs.Enums;

public enum FaceRecognitionField {
    MATCH_WITH_PAN ("Match with PAN face"),
    MATCH_WITH_AADHAR ("Match with AADHAR face"),
    LIVELINESS_CHECK ("Liveliness check");

    private String value;

    FaceRecognitionField(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static FaceRecognitionField EnumOfString(String value) {
        for (FaceRecognitionField panField : values()) {
            if (panField.value.equals(value)) {
                return panField;
            }
        }
        return null;
    }

}
