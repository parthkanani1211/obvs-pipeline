package ai.obvs.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CKYCProfileData")
public class CKYCProfileData extends GenericEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerNameId", referencedColumnName = "id")
    private NameFields customerName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fatherNameId", referencedColumnName = "id")
    private NameFields fatherName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motherNameId", referencedColumnName = "id")
    private NameFields motherName;

    private String accountType;

    private String maritalStatus;

    private String occupation;

    private String community;

    private String constitution;

    private String residency;

    private String nationality;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", referencedColumnName = "id")
    private AddressFields address;

    private boolean localAddressSameAsPermanent;

    private String refId;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CKYCProfileData that = (CKYCProfileData) o;
        return localAddressSameAsPermanent == that.localAddressSameAsPermanent &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(fatherName, that.fatherName) &&
                Objects.equals(motherName, that.motherName) &&
                Objects.equals(accountType, that.accountType) &&
                Objects.equals(maritalStatus, that.maritalStatus) &&
                Objects.equals(occupation, that.occupation) &&
                Objects.equals(community, that.community) &&
                Objects.equals(constitution, that.constitution) &&
                Objects.equals(residency, that.residency) &&
                Objects.equals(refId, that.refId) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName, fatherName, motherName, accountType, maritalStatus, occupation, community, constitution, residency, address, localAddressSameAsPermanent);
    }
}
