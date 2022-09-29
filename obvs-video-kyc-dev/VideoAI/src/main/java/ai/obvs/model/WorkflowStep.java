package ai.obvs.model;

import ai.obvs.Enums.WorkflowStepType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "WorkflowSteps", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name", "workflow_Id"
        })
})
public class WorkflowStep extends GenericEntity{

    private int orderNumber;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private ai.obvs.Enums.WorkflowStep workflowStep;

    @Enumerated(EnumType.STRING)
    private WorkflowStepType workflowStepType;

    @OneToOne
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public WorkflowStepType getWorkflowStepType() {
        return workflowStepType;
    }

    public void setWorkflowStepType(WorkflowStepType workflowStepType) {
        this.workflowStepType = workflowStepType;
    }

    public ai.obvs.Enums.WorkflowStep getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(ai.obvs.Enums.WorkflowStep workflowStep) {
        this.workflowStep = workflowStep;
    }
}

