package ai.obvs.dto.Aadhar;

public class AadharAddress {
    private String zip;
    private String care_of;
    private Address address;

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCare_of() {
        return care_of;
    }

    public void setCare_of(String care_of) {
        this.care_of = care_of;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return care_of + " " + address + " " + zip;
    }
}
