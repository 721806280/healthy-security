package com.healthy.security.core.validate.code;

import com.healthy.security.core.properties.SecurityProperties;
import com.healthy.security.core.validate.code.image.ImageCodeGenerator;
import com.healthy.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.healthy.security.core.validate.code.sms.SmsCodeGenerator;
import com.healthy.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 验证码相关的扩展点配置。配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Resource
    private SecurityProperties securityProperties;

    /**
     * 图片验证码图片生成器
     *
     * @return {@link ImageCodeGenerator}
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setImageCodeProperties(securityProperties.getCode().getImage());
        return codeGenerator;
    }

    /**
     * 短信验证码图片生成器
     *
     * @return {@link SmsCodeGenerator}
     */
    @Bean
    @ConditionalOnMissingBean(name = "smsValidateCodeGenerator")
    public ValidateCodeGenerator smsValidateCodeGenerator() {
        SmsCodeGenerator smsCodeGenerator = new SmsCodeGenerator();
        smsCodeGenerator.setSmsCodeProperties(securityProperties.getCode().getSms());
        return smsCodeGenerator;
    }

    /**
     * 短信验证码发送器
     *
     * @return {@link DefaultSmsCodeSender}
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
