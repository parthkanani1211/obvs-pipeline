package ai.obvs.model;

import ai.obvs.Enums.KYCResourceType;

import javax.persistence.*;

@Entity
@Table(name = "KYCResources")
public class KYCResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] content;

    @Enumerated(EnumType.STRING)
    private KYCResourceType kycResourceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public KYCResourceType getKycResourceType() {
        return kycResourceType;
    }

    public void setKycResourceType(KYCResourceType kycResourceType) {
        this.kycResourceType = kycResourceType;
    }
}
