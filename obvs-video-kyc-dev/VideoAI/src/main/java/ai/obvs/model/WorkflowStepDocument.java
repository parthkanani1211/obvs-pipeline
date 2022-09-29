package ai.obvs.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "workflowStepDocuments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "workflowStepId", "documentId"
        })
})
public class WorkflowStepDocument extends GenericEntity{

    private Long workflowStepId;

    private Long documentId;

    public Long getWorkflowStepId() {
        return workflowStepId;
    }

    public void setWorkflowStepId(Long workflowStepId) {
        this.workflowStepId = workflowStepId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}

