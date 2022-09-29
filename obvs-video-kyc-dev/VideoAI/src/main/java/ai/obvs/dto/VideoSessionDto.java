package ai.obvs.dto;

import ai.obvs.Enums.VideoSessionStatus;
import ai.obvs.model.Agent;
import ai.obvs.model.Customer;
import ai.obvs.model.VideoSession;

import java.time.ZonedDateTime;
import java.util.Set;

public class VideoSessionDto {
    private Long id;
    private String session;
    private VideoSessionStatus videoSessionStatus;
    private ZonedDateTime startedTime;
    private ZonedDateTime endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public VideoSessionStatus getVideoSessionStatus() {
        return videoSessionStatus;
    }

    public void setVideoSessionStatus(VideoSessionStatus videoSessionStatus) {
        this.videoSessionStatus = videoSessionStatus;
    }

    public ZonedDateTime getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(ZonedDateTime startedTime) {
        this.startedTime = startedTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}
