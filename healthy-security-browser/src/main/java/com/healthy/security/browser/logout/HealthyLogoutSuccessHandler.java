package com.healthy.security.browser.logout;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.healthy.security.core.properties.SecurityConstants;
import com.healthy.security.core.support.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.social.security.SocialUserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 默认的退出成功处理器，如果设置了healthy.security.browser.signOutUrl，则跳到配置的地址上，
 * 如果没配置，则返回json格式的响应。
 */
@Slf4j
public class HealthyLogoutSuccessHandler implements LogoutSuccessHandler {

    private String signOutSuccessUrl;

    public HealthyLogoutSuccessHandler(String signOutSuccessUrl) {
        this.signOutSuccessUrl = signOutSuccessUrl;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (Objects.isNull(authentication)) {
            log.debug("未登录.");
            response.setContentType(SecurityConstants.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONUtil.toJsonStr(new SimpleResponse("未登录")));
        } else {
            Object principal = authentication.getPrincipal();
            String userName = principal instanceof SocialUserDetails ? ((SocialUserDetails) principal).getUserId() : (String) principal;
            log.debug("当前用户：[{}] 退出成功.", userName);
            if (StrUtil.isBlank(signOutSuccessUrl)) {
                response.setContentType(SecurityConstants.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().write(JSONUtil.toJsonStr(new SimpleResponse("退出成功")));
            } else {
                response.sendRedirect(signOutSuccessUrl);
            }
        }
    }
}
