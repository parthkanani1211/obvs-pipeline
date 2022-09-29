package ai.obvs.services.notification.impl;

import ai.obvs.services.notification.NotificationService;
import ai.obvs.services.notification.PropertiesData;
import ai.obvs.services.notification.SMSService;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private PropertiesData propertiesData;

    @Autowired
    private SMSService smsService;

    public String sendVideoKYCInitURL(Long mobileNumber, String refId) {
        String appUrl = propertiesData.getAppUrl();
        String message = "Dear customer, please click on the below link to perform the Video KYC session. ";
        appUrl = appUrl + "/auth/login?mobileNumber=" + mobileNumber + "&refId=" + refId;
        smsService.sendMessage(mobileNumber, message + appUrl);
        return appUrl;
    }

    @Override
    public void sendOTP(Long mobileNumber, int otpCode) {
        String message = "Dear customer, your one time OTP is " + otpCode + "Please do not share it with anyone.";
        smsService.sendMessage(mobileNumber, message);
    }

    @Override
    public void notifyVKYCCompleted(Long mobileNumber) {
        String message = "Dear customer, thank you for using Video KYC service. Your Video KYC request has been completed successfully. It will be further validated by our Bank officers and you will receive notification message soon.";
        smsService.sendMessage(mobileNumber, message);
    }

    @Override
    public void notifyVKYCApproved(Long mobileNumber) {
        String message = "Dear customer, your KYC details are approved. Thank you for using Video KYC service.";
        smsService.sendMessage(mobileNumber, message);
    }

    @Override
    public void notifyVKYCRejected(Long mobileNumber) {
        String message = "Dear customer, your KYC details are not approved due to some data mismatch. You will receive message to do the Video KYC session again.";
        smsService.sendMessage(mobileNumber, message);
    }

}
