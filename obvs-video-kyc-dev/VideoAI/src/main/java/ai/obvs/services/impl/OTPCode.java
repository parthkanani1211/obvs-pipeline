package ai.obvs.services.impl;

import java.time.ZonedDateTime;

public class OTPCode {
    private String code;
    private ZonedDateTime expireTime;

    public OTPCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = ZonedDateTime.now().plusSeconds(expireIn);
    }

    public OTPCode(String code, ZonedDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpire() {
        return ZonedDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ZonedDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
