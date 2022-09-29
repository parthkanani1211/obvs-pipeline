package ai.obvs.model;

import ai.obvs.Enums.VideoKYCRequestStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "videoKYC")
public class VideoKYC extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonManagedReference
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "auditor_id")
    private Agent auditor;

    @NotNull
    private Long mobileNumber;

    @Enumerated(EnumType.STRING)
    private VideoKYCRequestStatus videoKYCRequestStatus;

    @OneToMany(mappedBy = "videoKYC", cascade = CascadeType.ALL)
    private List<VideoSession> videoSessions;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    private String comment;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public VideoKYCRequestStatus getVideoKYCRequestStatus() {
        return videoKYCRequestStatus;
    }

    public void setVideoKYCRequestStatus(VideoKYCRequestStatus videoKYCRequestStatus) {
        this.videoKYCRequestStatus = videoKYCRequestStatus;
    }

    public List<VideoSession> getVideoSessions() {
        return videoSessions;
    }

    public void setVideoSessions(List<VideoSession> videoSessions) {
        this.videoSessions = videoSessions;
    }

//    public VideoSession getVideoSession() {
//        return videoSessions.get(0);
//    }
//
//    public void setVideoSession(VideoSession videoSession) {
//        this.videoSessions.add(videoSession);
//    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Agent getAuditor() {
        return auditor;
    }

    public void setAuditor(Agent auditor) {
        this.auditor = auditor;
    }
}
