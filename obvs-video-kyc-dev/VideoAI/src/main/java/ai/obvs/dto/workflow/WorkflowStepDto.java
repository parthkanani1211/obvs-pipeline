package ai.obvs.dto.workflow;

import ai.obvs.Enums.DocumentName;
import ai.obvs.Enums.WorkflowStep;
import ai.obvs.Enums.WorkflowStepType;
import ai.obvs.dto.GenericEntityDto;

import java.util.List;

public class WorkflowStepDto {
    private Long id;
    private int orderNumber;
    private String name;
    private WorkflowStepType workflowStepType;
    private WorkflowStep workflowStep;
    private int inputImages;
    private List<DocumentDto> allowedDocuments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getInputImages() {
        return inputImages;
    }

    public void setInputImages(int inputImages) {
        this.inputImages = inputImages;
    }

    public WorkflowStep getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(WorkflowStep workflowStep) {
        this.workflowStep = workflowStep;
    }

    public WorkflowStepType getWorkflowStepType() {
        return workflowStepType;
    }

    public void setWorkflowStepType(WorkflowStepType workflowStepType) {
        this.workflowStepType = workflowStepType;
    }

    public List<DocumentDto> getAllowedDocuments() {
        return allowedDocuments;
    }

    public void setAllowedDocuments(List<DocumentDto> allowedDocuments) {
        this.allowedDocuments = allowedDocuments;
    }
}
