package com.healthy.security.core.social;

import cn.hutool.core.util.StrUtil;
import com.healthy.security.core.properties.SecurityProperties;
import com.healthy.security.core.social.connect.jdbc.HealthyJdbcUsersConnectionRepository;
import com.healthy.security.core.social.support.HealthySpringSocialConfigurer;
import com.healthy.security.core.social.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交登录配置主类
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        HealthyJdbcUsersConnectionRepository usersConnectionRepository = new HealthyJdbcUsersConnectionRepository(
                dataSource, connectionFactoryLocator, Encryptors.noOpText());

        String jdbcTablePrefix = securityProperties.getSocial().getJdbcTablePrefix();
        if (StrUtil.isNotBlank(jdbcTablePrefix)) {
            usersConnectionRepository.setTablePrefix(jdbcTablePrefix);
        }
        if (connectionSignUp != null) {
            usersConnectionRepository.setConnectionSignUp(connectionSignUp);
        }
        return usersConnectionRepository;
    }

    /**
     * 社交登录配置类，供浏览器或app模块引入设计登录配置用。
     *
     * @return {@link SpringSocialConfigurer}
     */
    @Bean
    public SpringSocialConfigurer healthySocialSecurityConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        HealthySpringSocialConfigurer configurer = new HealthySpringSocialConfigurer(filterProcessesUrl);
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return configurer;
    }

    /**
     * 用来处理注册流程的工具类
     *
     * @param connectionFactoryLocator
     * @return {@link ProviderSignInUtils}
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }

    /**
     * Constructs a ConnectController.
     *
     * @param connectionFactoryLocator the locator for {@link ConnectionFactory} instances needed to establish connections
     * @param connectionRepository     the current user's {@link ConnectionRepository} needed to persist connections; must be a proxy to a request-scoped bean
     */
    @Bean
    @ConditionalOnMissingBean({ConnectController.class})
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }
}
