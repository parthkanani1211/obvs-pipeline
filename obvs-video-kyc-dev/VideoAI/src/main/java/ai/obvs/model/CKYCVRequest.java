package ai.obvs.model;

import javax.persistence.*;

@Entity
@Table(name = "CKYCVRequest")
public class CKYCVRequest extends GenericEntity {

    private String refId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "VideoKYCId", referencedColumnName = "id")
    private VideoKYC videoKYC;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public VideoKYC getVideoKYC() {
        return videoKYC;
    }

    public void setVideoKYC(VideoKYC videoKYC) {
        this.videoKYC = videoKYC;
    }
}
