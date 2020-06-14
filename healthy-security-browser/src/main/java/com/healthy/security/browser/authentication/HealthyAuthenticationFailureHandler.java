package com.healthy.security.browser.authentication;

import cn.hutool.json.JSONUtil;
import com.healthy.security.core.properties.LoginResponseType;
import com.healthy.security.core.properties.SecurityConstants;
import com.healthy.security.core.properties.SecurityProperties;
import com.healthy.security.core.support.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录失败处理器
 */
@Slf4j
@Component("healthyAuthenticationFailureHandler")
public class HealthyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("登录失败, 原因: {}", exception.getMessage());
        if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getSignInResponseType())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(SecurityConstants.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONUtil.toJsonStr(new SimpleResponse(exception.getMessage())));
        } else {
            setDefaultFailureUrl(securityProperties.getBrowser().getSignInPage());
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}