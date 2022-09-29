package ai.obvs.dto.Aadhar;

public class Address {
    private String country;
    private String dist;
    private String state;
    private String po;
    private String loc;
    private String vtc;
    private String subdist;
    private String street;
    private String house;
    private String landmark;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public String getSubdist() {
        return subdist;
    }

    public void setSubdist(String subdist) {
        this.subdist = subdist;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    @Override
    public String toString() {
//        return "country='" + country + '\'' +
//                ", dist='" + dist + '\'' +
//                ", state='" + state + '\'' +
//                ", po='" + po + '\'' +
//                ", loc='" + loc + '\'' +
//                ", vtc='" + vtc + '\'' +
//                ", subdist='" + subdist + '\'' +
//                ", street='" + street + '\'' +
//                ", house='" + house + '\'' +
//                ", landmark='" + landmark + '\'';

        return " " + house + " " + street + " " + loc + " " + landmark + " " + vtc + " "
                + po + " " + dist + " " + state + " " + country;
    }
}
