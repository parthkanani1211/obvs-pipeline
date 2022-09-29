package ai.obvs.dto;

import ai.obvs.Enums.VideoKYCRequestStatus;

import java.time.ZonedDateTime;

public class VideoKYCRequestBaseDto extends GenericEntityDto {
    private Long mobileNumber;
    private VideoKYCRequestStatus videoKYCRequestStatus;
    private String sessionName;

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

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
