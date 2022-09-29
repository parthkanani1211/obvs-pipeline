package ai.obvs.model;

public class AadharInfo {
    private String number;
    private String name;
    private String birthDate;
    private String address;
    private String gender;
    private String faceImageData;
    private String frontImageData;
    private String backImageData;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFaceImageData() {
        return faceImageData;
    }

    public void setFaceImageData(String faceImageData) {
        this.faceImageData = faceImageData;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFrontImageData() {
        return frontImageData;
    }

    public void setFrontImageData(String frontImageData) {
        this.frontImageData = frontImageData;
    }

    public String getBackImageData() {
        return backImageData;
    }

    public void setBackImageData(String backImageData) {
        this.backImageData = backImageData;
    }
}
