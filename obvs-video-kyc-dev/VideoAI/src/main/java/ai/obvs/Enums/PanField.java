package ai.obvs.Enums;

import ai.obvs.integration.models.Pan;

public enum PanField {
    NUMBER ("Number"),
    NAME ("Name"),
    PARENT_NAME("Parent Name"),
    DOB ("Date of Birth");

    private String value;

    PanField(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static PanField EnumOfString(String value) {
        for (PanField panField : values()) {
            if (panField.value.equals(value)) {
                return panField;
            }
        }
        return null;
    }

}
