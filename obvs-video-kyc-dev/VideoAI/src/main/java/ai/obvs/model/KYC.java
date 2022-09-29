package ai.obvs.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "KYC")
public class KYC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "workflowStep_id", nullable = false)
    private WorkflowStep workflowStep;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Lob
    private String KYCVerificationData;

    private boolean isVerified;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "KYC_resources",
            joinColumns = @JoinColumn(name = "KYC_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private Set<KYCResource> resources;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "videoSession_id", nullable = false)
    private VideoSession videoSession;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "videoKYC_id", nullable = false)
//    private VideoKYC videoKYC;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowStep getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(WorkflowStep workflowStep) {
        this.workflowStep = workflowStep;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Set<KYCResource> getResources() {
        return resources;
    }

    public void setResources(Set<KYCResource> resources) {
        this.resources = resources;
    }

    public VideoSession getVideoSession() {
        return videoSession;
    }

    public void setVideoSession(VideoSession videoSession) {
        this.videoSession = videoSession;
    }

    public String getKYCVerificationData() {
        return KYCVerificationData;
    }

    public void setKYCVerificationData(String KYCVerificationData) {
        this.KYCVerificationData = KYCVerificationData;
    }

}
