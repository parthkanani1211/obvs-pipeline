package ai.obvs.repository;

import ai.obvs.model.Organization;
import ai.obvs.model.Workflow;
import ai.obvs.model.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {
    List<WorkflowStep> findAllByWorkflow(Workflow workflow);
}
