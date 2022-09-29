package ai.obvs.repository;

import ai.obvs.model.Organization;
import ai.obvs.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    List<Workflow> findAllByOrganization(Organization organization);
    Optional<Workflow> findByOrganizationAndName(Organization organization, String name);
}
