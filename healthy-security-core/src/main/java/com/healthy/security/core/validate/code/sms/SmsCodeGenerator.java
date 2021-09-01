package com.healthy.security.core.validate.code.sms;

import cn.hutool.core.util.RandomUtil;
import com.healthy.security.core.properties.SmsCodeProperties;
import com.healthy.security.core.validate.code.ValidateCode;
import com.healthy.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码生成器
 */
public class SmsCodeGenerator implements ValidateCodeGenerator {

    private SmsCodeProperties smsCodeProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomUtil.randomString(smsCodeProperties.getLength());
        return new ValidateCode(code, smsCodeProperties.getExpireIn());
    }

    public void setSmsCodeProperties(SmsCodeProperties smsCodeProperties) {
        this.smsCodeProperties = smsCodeProperties;
    }
}
