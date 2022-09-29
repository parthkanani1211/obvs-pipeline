package ai.obvs.Enums;

public enum AadharField {
    NUMBER ("Number"),
    NAME ("Name"),
    GENDER("Gender"),
    DOB ("Date of Birth"),
    ADDRESS ("Address");

    private String value;

    AadharField(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static AadharField EnumOfString(String value) {
        for (AadharField aadharField : values()) {
            if (aadharField.value.equals(value)) {
                return aadharField;
            }
        }
        return null;
    }
}
