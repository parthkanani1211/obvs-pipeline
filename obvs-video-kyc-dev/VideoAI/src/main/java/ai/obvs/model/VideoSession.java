package ai.obvs.model;

import ai.obvs.Enums.VideoSessionStatus;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "videoSessions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "session"
        })
})
public class VideoSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    @Enumerated(EnumType.STRING)
    private VideoSessionStatus status;

    private String session;

    private String recordingURL;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kycResource_id", referencedColumnName = "id")
    private KYCResource kycResource;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "videoKYC_id", nullable = false)
    private VideoKYC videoKYC;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public VideoSessionStatus getStatus() {
        return status;
    }

    public void setStatus(VideoSessionStatus status) {
        this.status = status;
    }

    public VideoKYC getVideoKYC() {
        return videoKYC;
    }

    public void setVideoKYC(VideoKYC videoKYC) {
        this.videoKYC = videoKYC;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getRecordingURL() {
        return recordingURL;
    }

    public void setRecordingURL(String recordingURL) {
        this.recordingURL = recordingURL;
    }

    public KYCResource getKycResource() {
        return kycResource;
    }

    public void setKycResource(KYCResource kycResource) {
        this.kycResource = kycResource;
    }
}
