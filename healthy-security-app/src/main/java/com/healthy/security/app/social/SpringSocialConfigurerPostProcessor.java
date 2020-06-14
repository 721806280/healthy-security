package com.healthy.security.app.social;

import cn.hutool.core.util.StrUtil;
import com.healthy.security.core.properties.SecurityConstants;
import com.healthy.security.core.social.support.HealthySpringSocialConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author zhang.xiaoming
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StrUtil.equals(beanName, "healthySocialSecurityConfig")) {
            HealthySpringSocialConfigurer config = (HealthySpringSocialConfigurer) bean;
            config.signupUrl(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL);
            return config;
        }
        return bean;
    }

}
