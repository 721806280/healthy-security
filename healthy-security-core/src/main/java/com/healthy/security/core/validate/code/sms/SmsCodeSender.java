package com.healthy.security.core.validate.code.sms;

public interface SmsCodeSender {

    /**
     * 短信验证码发送
     *
     * @param mobile 手机号
     * @param code   验证码
     */
    void send(String mobile, String code);

}
