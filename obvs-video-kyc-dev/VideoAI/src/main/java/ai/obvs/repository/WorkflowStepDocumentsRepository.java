package ai.obvs.repository;

import ai.obvs.model.Workflow;
import ai.obvs.model.WorkflowStep;
import ai.obvs.model.WorkflowStepDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowStepDocumentsRepository extends JpaRepository<WorkflowStepDocument, Long> {
    List<WorkflowStepDocument> findAllByWorkflowStepId(Long workflowStepId);
}
