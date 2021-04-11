package com.healthy.security.core.authentication;

import com.healthy.security.core.properties.SecurityConstants;
import com.healthy.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * FormAuthenticationConfig
 *
 * @author xiaomingzhang
 */
@Component
public class FormAuthenticationConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler healthyAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler healthyAuthenticationFailureHandler;

    public void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(healthyAuthenticationSuccessHandler)
                .failureHandler(healthyAuthenticationFailureHandler);

        if (securityProperties.isEnableAuditLog()) {
            http.addFilterBefore(new HealthyAuditLogFilter(), ExceptionTranslationFilter.class);
        }
    }
}