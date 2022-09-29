package ai.obvs.Enums;

public enum WorkflowStep {
    FACE ("FACE"),
    SIGNATURE ("SIGN");

    private String value;

    WorkflowStep(String value){
        this.value = value;
    }

    public String getName(){
        return this.name();
    }

    public String getValue(){
        return this.value;
    }

    public static WorkflowStep EnumOfString(String value) {
        for (WorkflowStep workflowStep : values()) {
            if (workflowStep.value.equals(value)) {
                return workflowStep;
            }
        }
        return null;
    }
}
