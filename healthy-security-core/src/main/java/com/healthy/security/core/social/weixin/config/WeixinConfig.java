package com.healthy.security.core.social.weixin.config;

import com.healthy.security.core.properties.SecurityProperties;
import com.healthy.security.core.properties.WeixinProperties;
import com.healthy.security.core.social.view.HealthyConnectView;
import com.healthy.security.core.social.weixin.connet.WeixinOAuth2ConnectionFactory;
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
 * 微信登录配置
 */
@Configuration
@ConditionalOnProperty(prefix = "healthy.security.social.weixin", name = "app-id")
public class WeixinConfig extends SocialConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixinProperties = securityProperties.getSocial().getWeixin();
        return new WeixinOAuth2ConnectionFactory(weixinProperties.getProviderId(), weixinProperties.getAppId(),
                weixinProperties.getAppSecret());
    }

    /**
     * connect/qqConnect格式为： connect/{providerId}Connect
     * 配置默认微信绑定/解绑成功的返回页
     *
     * @return
     */
    @Bean({"connect/weixinConnect", "connect/weixinConnected"})
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView() {
        return new HealthyConnectView();
    }

}
