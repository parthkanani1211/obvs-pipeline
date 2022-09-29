package ai.obvs.integration.models;


public class PanResponse {
  private String fileName;
  private String requestId;
  private String requestTime;
  private Pan pan;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(String requestTime) {
    this.requestTime = requestTime;
  }

  public Pan getPan() {
    return pan;
  }

  public void setPan(Pan pan) {
    this.pan = pan;
  }
}
