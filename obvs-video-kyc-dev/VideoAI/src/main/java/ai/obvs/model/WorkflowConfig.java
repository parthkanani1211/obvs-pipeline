package ai.obvs.model;

import ai.obvs.Enums.WorkflowStepInputType;
import ai.obvs.Enums.WorkflowStepType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "workflowConfig")
public class WorkflowConfig extends GenericEntity {

    private String name;

    private String description;

    private String imageLabel;

    @Column(columnDefinition = "integer default 0")
    private int imageCount;

    @Enumerated(EnumType.STRING)
    private WorkflowStepType workflowStepType;

    @Enumerated(EnumType.STRING)
    private WorkflowStepInputType workflowStepInputType;

    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public WorkflowStepType getWorkflowStepType() {
        return workflowStepType;
    }

    public void setWorkflowStepType(WorkflowStepType workflowStepType) {
        this.workflowStepType = workflowStepType;
    }

    public WorkflowStepInputType getWorkflowStepInputType() {
        return workflowStepInputType;
    }

    public void setWorkflowStepInputType(WorkflowStepInputType workflowStepInputType) {
        this.workflowStepInputType = workflowStepInputType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }
}

