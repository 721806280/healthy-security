package com.healthy.security.app.authentication;

import cn.hutool.json.JSONUtil;
import com.healthy.security.core.properties.SecurityConstants;
import com.healthy.security.core.support.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("登录失败, 原因: {}", exception.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType(SecurityConstants.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JSONUtil.toJsonStr(new SimpleResponse(exception.getMessage())));
    }
}