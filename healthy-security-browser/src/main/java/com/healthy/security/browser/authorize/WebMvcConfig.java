package com.healthy.security.browser.authorize;

import cn.hutool.core.util.StrUtil;
import com.healthy.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 默认登录页
        String signInPage = securityProperties.getBrowser().getSignInPage();
        registry.addViewController(signInPage).setViewName(signInPage);

        // 登录成功后跳转的地址，如果设置了此属性，则登录成功后总是会跳到这个地址上。
        String singInSuccessUrl = securityProperties.getBrowser().getSingInSuccessUrl();
        if (StrUtil.isNotBlank(singInSuccessUrl)) {
            registry.addViewController(singInSuccessUrl).setViewName(singInSuccessUrl);
        }

        // Session失效时跳转的地址
        String sessionInvalidUrl = securityProperties.getBrowser().getSession().getSessionInvalidUrl();
        registry.addViewController(sessionInvalidUrl).setViewName(sessionInvalidUrl);

        // 退出登录页，退出成功时跳转的url，如果配置了，则跳到指定的url，如果没配置，则返回json数据。
        String singOutSuccessUrl = securityProperties.getBrowser().getLogOutSuccessUrl();
        if (StrUtil.isNotBlank(singOutSuccessUrl)) {
            registry.addViewController(singOutSuccessUrl).setViewName(singOutSuccessUrl);
        }

        // 社交登录，如果需要用户注册，跳转的页面
        String signUpUrl = securityProperties.getBrowser().getSignUpUrl();
        registry.addViewController(signUpUrl).setViewName(signUpUrl);

        // 绑定社交账号信息页面
        String bindingSocialUrl = securityProperties.getBrowser().getBindingSocialUrl();
        registry.addViewController(bindingSocialUrl).setViewName(bindingSocialUrl);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
    }
}
