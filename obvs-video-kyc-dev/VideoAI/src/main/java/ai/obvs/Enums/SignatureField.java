package ai.obvs.Enums;

public enum SignatureField {
    IS_VALID_SIGN ("Valid signature"),
    MATCH_WITH_PAN ("Match with PAN signature");

    private String value;

    SignatureField(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static SignatureField EnumOfString(String value) {
        for (SignatureField panField : values()) {
            if (panField.value.equals(value)) {
                return panField;
            }
        }
        return null;
    }

}
