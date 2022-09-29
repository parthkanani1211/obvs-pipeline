package ai.obvs.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CBRequests")
public class CBRequest extends GenericEntity {

    private Long mobileNumber;
    private String refId;

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }
}
