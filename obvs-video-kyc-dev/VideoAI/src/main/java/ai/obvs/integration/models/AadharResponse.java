package ai.obvs.integration.models;

public class AadharResponse {
  private String fileName;
  private String reuqestId;
  private String requestTime;
  private Aadhar aadhar;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getReuqestId() {
    return reuqestId;
  }

  public void setReuqestId(String reuqestId) {
    this.reuqestId = reuqestId;
  }

  public String getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(String requestTime) {
    this.requestTime = requestTime;
  }

  public Aadhar getAadhar() {
    return aadhar;
  }

  public void setAadhar(Aadhar aadhar) {
    this.aadhar = aadhar;
  }
}
