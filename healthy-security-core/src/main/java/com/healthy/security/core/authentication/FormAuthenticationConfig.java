package com.healthy.security.core.authentication;

import com.healthy.security.core.processor.UserDetailsProcessorHolder;
import com.healthy.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 表单登录配置
 */
@Component
public class FormAuthenticationConfig {

    @Autowired
    protected AuthenticationSuccessHandler healthyAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler healthyAuthenticationFailureHandler;

    @Autowired
    protected UserDetailsProcessorHolder userDetailsProcessorHolder;

    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new HealthyAuditLogFilter(userDetailsProcessorHolder), ExceptionTranslationFilter.class)
                .formLogin() // 身份认证的方式 - 表单登录
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(healthyAuthenticationSuccessHandler)
                .failureHandler(healthyAuthenticationFailureHandler);
    }
}
