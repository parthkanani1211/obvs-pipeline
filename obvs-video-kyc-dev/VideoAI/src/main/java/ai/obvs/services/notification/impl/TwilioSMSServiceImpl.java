package ai.obvs.services.notification.impl;

import ai.obvs.services.notification.SMSService;
import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSMSServiceImpl implements SMSService {


    public TwilioSMSServiceImpl() {

    }

    @Override
    public void sendOTP(Long mobileNumber, int otpNumber) {
        Message message = Message.creator(new PhoneNumber(String.valueOf(mobileNumber)),
                new PhoneNumber("+14792390764"),
                String.valueOf(otpNumber)).create();
    }

    @Override
    public void sendMessage(Long mobileNumber, String message) {
        Twilio.init("ACc7bb8e5bbe4532cfcfa893d36fda3cae", "03bf6a6452204437d94f027f7284df71");
        TwilioRestClient TWILIO_CLIENT = Twilio.getRestClient();
        Message message1 = Message.creator(new PhoneNumber("+91" + mobileNumber.toString()),"MG7051f87865e175e41f3eb6f4d2dac299",
                message)
                .create();

        System.out.println(message1.getSid());
    }
}
