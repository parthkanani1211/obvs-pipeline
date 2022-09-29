package ai.obvs.services.notification;

import org.springframework.beans.factory.annotation.Value;

public class PropertiesData {
    @Value("${app.url}")
    private String appUrl;

    @Value("${app.smsEnabled}")
    private String smsEnabled;

    @Value("${app.ckycEnabled}")
    private String ckycEnabled;

    public String getAppUrl() {
        if (appUrl.isBlank()) {
            this.appUrl = "http://localhost:4747";
        }
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public boolean getSmsEnabled() {
        return Boolean.valueOf(smsEnabled);
    }

    public boolean getCkycEnabled() {
        return Boolean.valueOf(ckycEnabled);
    }
}
