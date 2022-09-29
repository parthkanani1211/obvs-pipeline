package ai.obvs.repository;

import ai.obvs.Enums.Country;
import ai.obvs.Enums.DocumentName;
import ai.obvs.model.Document;
import ai.obvs.model.Organization;
import ai.obvs.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    //    Optional<Document> findByWorkflowStep(Workflow workflow);
//    List<Document> findAllByWorkflowStepIn(List<Workflow> workflows);
//    List<WorkflowDocumentDetail> findAllById(List<WorkflowStep> workflowSteps);
    Optional<Document> findByCountryAndName(Country country, DocumentName documentName);
    List<Document> findAllByCountryAndNameIn(Country country, List<DocumentName> documentName);
    List<Document> findAllByCountryAndIdIn(Country country, List<Long> documentIds);
    List<Document> findAllByCountry(Country country);
}
