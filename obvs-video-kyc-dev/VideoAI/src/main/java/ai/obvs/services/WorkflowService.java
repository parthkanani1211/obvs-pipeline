package ai.obvs.services;

import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.*;
import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.dto.workflow.WorkflowDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.model.Document;
import ai.obvs.model.Workflow;
import ai.obvs.model.WorkflowStep;

import java.util.List;
import java.util.Optional;

public interface WorkflowService {
    WorkflowDto save(Long orgId, WorkflowDto workflowDto);

    void add(Long orgId, String workflowName, WorkflowStepDto workflowStepDto);

    void addAll(Long orgId, String workflowName, List<WorkflowStepDto> workflowStepDtoList);

    void addAll(Long orgId, String workflowName, Long stepId, List<DocumentName> documentNames);

    WorkflowDto getWorkflow(Long orgId, String workflowName);

    List<Document> getAll(Long orgId, String workflowName, Long stepId);

    List<WorkflowStepDto> getWorkflowSteps(Long orgId, String workflowName);

    Optional<WorkflowStep> getWorkflowStep(Long orgId, String workflowName, Long stepId);

    Optional<WorkflowStep> getWorkflowStep(Long orgId, String workflowName, String workflowStepName);
}
