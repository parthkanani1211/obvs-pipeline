package ai.obvs.mapper;

import ai.obvs.dto.workflow.WorkflowDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.model.Workflow;
import ai.obvs.model.WorkflowStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkflowMapper {
    WorkflowMapper MAPPER = Mappers.getMapper(WorkflowMapper.class);

    Workflow ToWorkflow(WorkflowDto workflowDto);

    WorkflowDto ToWorkflowDto(Workflow workflow);
}
