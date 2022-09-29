package ai.obvs.services.CKYC.model;

public class CKYCResponseData {
    private String action;
    private String ref_id;
    private RequestData request_data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public RequestData getRequest_data() {
        return request_data;
    }

    public void setRequest_data(RequestData request_data) {
        this.request_data = request_data;
    }
}
