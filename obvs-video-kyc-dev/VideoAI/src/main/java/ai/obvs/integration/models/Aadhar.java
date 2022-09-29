package ai.obvs.integration.models;

public class Aadhar {
  private Control id;
  private Control name;
  private Control rn;
  private Control Gender;
  private Control birthDate;
  private Control address;
  private Control qr;
  private Control photo;

  public Control getId() {
    return id;
  }

  public void setId(Control id) {
    this.id = id;
  }

  public Control getName() {
    return name;
  }

  public void setName(Control name) {
    this.name = name;
  }

  public Control getRn() {
    return rn;
  }

  public void setRn(Control rn) {
    this.rn = rn;
  }

  public Control getGender() {
    return Gender;
  }

  public void setGender(Control gender) {
    Gender = gender;
  }

  public Control getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Control birthDate) {
    this.birthDate = birthDate;
  }

  public Control getAddress() {
    return address;
  }

  public void setAddress(Control address) {
    this.address = address;
  }

  public Control getQr() {
    return qr;
  }

  public void setQr(Control qr) {
    this.qr = qr;
  }

  public Control getPhoto() {
    return photo;
  }

  public void setPhoto(Control photo) {
    this.photo = photo;
  }
}
