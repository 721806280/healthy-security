package com.healthy.security.core.social.weibo.config;

import com.healthy.security.core.properties.SecurityProperties;
import com.healthy.security.core.properties.WeiboProperties;
import com.healthy.security.core.social.view.HealthyConnectView;
import com.healthy.security.core.social.weibo.connet.WeiboOAuth2ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

/**
 * autoconfigure2.04中已经不存在social的自动配置类了
 * org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter
 * 当配置了app-id的时候才启用
 *
 * @ConditionalOnProperty(prefix = "healthy.security.social.weibo", name = "app-id")
 */
@Configuration
@ConditionalOnProperty(prefix = "healthy.security.social.weibo", name = "app-id")
public class WeiboConfig extends SocialConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    public ConnectionFactory<?> createConnectionFactory() {
        WeiboProperties weibo = securityProperties.getSocial().getWeibo();
        return new WeiboOAuth2ConnectionFactory(weibo.getProviderId(), weibo.getAppId(), weibo.getAppSecret());
    }

    /**
     * connect/qqConnect格式为： connect/{providerId}Connect
     * 配置默认QQ绑定/解绑成功的返回页
     *
     * @return
     */
    @Bean({"connect/weiboConnect", "connect/weiboConnected"})
    @ConditionalOnMissingBean(name = "weiboConnectedView")
    public View qqConnectedView() {
        return new HealthyConnectView();
    }
}
