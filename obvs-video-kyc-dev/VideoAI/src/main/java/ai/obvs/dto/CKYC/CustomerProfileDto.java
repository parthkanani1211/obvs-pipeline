package ai.obvs.dto.CKYC;

import ai.obvs.dto.GenericEntityDto;

public class CustomerProfileDto extends GenericEntityDto {

    private NameFields customerName;

    private NameFields fatherName;

    private NameFields motherName;

    private String accountType;

    private String maritalStatus;

    private String occupation;

    private String community;

    private String constitution;

    private String residency;

    private AddressFields address;

    private boolean localAddressSameAsPermanent;

    private String refId;

    public NameFields getCustomerName() {
        return customerName;
    }

    public void setCustomerName(NameFields customerName) {
        this.customerName = customerName;
    }

    public NameFields getFatherName() {
        return fatherName;
    }

    public void setFatherName(NameFields fatherName) {
        this.fatherName = fatherName;
    }

    public NameFields getMotherName() {
        return motherName;
    }

    public void setMotherName(NameFields motherName) {
        this.motherName = motherName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getConstitution() {
        return constitution;
    }

    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public String getResidency() {
        return residency;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public AddressFields getAddress() {
        return address;
    }

    public void setAddress(AddressFields address) {
        this.address = address;
    }

    public boolean isLocalAddressSameAsPermanent() {
        return localAddressSameAsPermanent;
    }

    public void setLocalAddressSameAsPermanent(boolean localAddressSameAsPermanent) {
        this.localAddressSameAsPermanent = localAddressSameAsPermanent;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }
}
