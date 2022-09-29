package ai.obvs.model;

import javax.persistence.*;

@Entity
@Table(name = "CKYCProfileVKYCRequest")
public class CKYCProfileVKYCRequest extends GenericEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CKYCVRequestId", referencedColumnName = "id")
    private CKYCVRequest ckycvRequest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CKYCProfileDataId", referencedColumnName = "id")
    private CKYCProfileData ckycProfileData;

    public CKYCVRequest getCkycvRequest() {
        return ckycvRequest;
    }

    public void setCkycvRequest(CKYCVRequest ckycvRequest) {
        this.ckycvRequest = ckycvRequest;
    }

    public CKYCProfileData getCkycProfileData() {
        return ckycProfileData;
    }

    public void setCkycProfileData(CKYCProfileData ckycProfileData) {
        this.ckycProfileData = ckycProfileData;
    }

}
