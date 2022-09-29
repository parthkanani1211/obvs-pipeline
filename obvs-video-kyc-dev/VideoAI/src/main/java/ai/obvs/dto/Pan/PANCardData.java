package ai.obvs.dto.Pan;

public class PANCardData {
    private String number;
    private String name;
    private String parentName;
    private String birthDate;

    public PANCardData(){
        number= "";
        name="";
        parentName= "";
        birthDate = "";
    }

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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

}
