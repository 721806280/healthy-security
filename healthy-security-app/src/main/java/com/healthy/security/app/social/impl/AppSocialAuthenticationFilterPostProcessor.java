package com.healthy.security.app.social.impl;

import com.healthy.security.core.social.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * APP环境下社交登录后置处理器
 */
@Component
public class AppSocialAuthenticationFilterPostProcessor implements SocialAuthenticationFilterPostProcessor {
    @Autowired
    private AuthenticationSuccessHandler healthyAuthenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
        // 交由自定义成功处理器处理，默认处理器为跳转
        socialAuthenticationFilter.setAuthenticationSuccessHandler(healthyAuthenticationSuccessHandler);
    }
}
