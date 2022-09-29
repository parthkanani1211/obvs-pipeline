package ai.obvs.Enums;

public enum Roles {
    ADMIN("Admin"),
    MAKER("Maker"),
    AUDITOR("Auditor"),
    CUSTOMER("Customer"),
    CHECKER("Checker"),
    SUPERADMIN("SuperAdmin"),
    APIUSER("APIUser");


    private String value;

    Roles(String roleValue) {
        this.value = roleValue;
    }

    public String getValue() {
        return value;
    }
}
