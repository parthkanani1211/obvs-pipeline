package ai.obvs.mapper;

import ai.obvs.dto.workflow.DocumentDto;
import ai.obvs.dto.workflow.WorkflowStepDto;
import ai.obvs.model.Document;
import ai.obvs.model.WorkflowStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface WorkflowStepMapper {
    WorkflowStepMapper MAPPER = Mappers.getMapper(WorkflowStepMapper.class);

    WorkflowStep ToWorkflowStep(WorkflowStepDto workflowStepDto);
    List<WorkflowStep> ToWorkflowStepList(List<WorkflowStepDto> workflowStepDtoList);

    @Mapping(target = "allowedDocuments", ignore = true)
    WorkflowStepDto ToWorkflowStepDto(WorkflowStep workflowStep);
    List<WorkflowStepDto> ToWorkflowStepDtoList(List<WorkflowStep> workflowStepList);
}
