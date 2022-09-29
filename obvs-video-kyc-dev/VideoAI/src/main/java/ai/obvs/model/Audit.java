package ai.obvs.model;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "Audits", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "videoKYCId"
        })
})
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long agentId;

    private Long customerId;

    private Long videoKYCId;

    private ZonedDateTime requestTime;

    private ZonedDateTime completedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getVideoKYCId() {
        return videoKYCId;
    }

    public void setVideoKYCId(Long videoKYCId) {
        this.videoKYCId = videoKYCId;
    }

    public ZonedDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public ZonedDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(ZonedDateTime completedTime) {
        this.completedTime = completedTime;
    }
}
