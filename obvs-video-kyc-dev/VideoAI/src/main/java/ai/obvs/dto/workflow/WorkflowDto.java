package ai.obvs.dto.workflow;

import ai.obvs.dto.GenericEntityDto;

import java.util.List;

public class WorkflowDto {
    private Long id;
    private String name;
    private List<WorkflowStepDto> steps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WorkflowStepDto> getSteps() {
        return steps;
    }

    public void setSteps(List<WorkflowStepDto> steps) {
        this.steps = steps;
    }
}
