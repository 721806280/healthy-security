package com.healthy.security.server;

import cn.hutool.core.util.ArrayUtil;
import com.healthy.security.core.properties.ClientDetailStoreType;
import com.healthy.security.core.properties.OAuth2ClientProperties;
import com.healthy.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器配置
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class HealthyAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired
    private ClientDetailsService clientDetails;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebResponseExceptionTranslator healthyResponseExceptionTranslator;

    /**
     * 认证及token配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 处理 ExceptionTranslationFilter 抛出的异常
                .exceptionTranslator(healthyResponseExceptionTranslator)
                // 认证管理器
                .authenticationManager(authenticationManager)
                // 指定token存储位置
                .tokenStore(tokenStore)
                // 要使用refresh_token的话，需要额外配置userDetailsService
                .userDetailsService(userDetailsService)
                .setClientDetailsService(clientDetails);

        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }

    }

    /**
     * tokenKey的访问权限表达式配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
    }

    /**
     * 客户端配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        if (securityProperties.getOauth2().getClientDetailStoreType().equals(ClientDetailStoreType.JDBC)) {
            log.debug("数据库模式加载ClientDetails.");
            clients.withClientDetails(clientDetails);
        } else {
            log.debug("内存模式加载ClientDetails.");
            InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
            if (ArrayUtil.isNotEmpty(securityProperties.getOauth2().getClients())) {
                for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
                    ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder clientBuilder = builder.withClient(client.getClientId());
                    clientBuilder.secret(passwordEncoder.encode(client.getClientSecret()))
                            .authorizedGrantTypes(client.getAuthorizedGrantTypes())
                            .accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
                            .refreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
                    if (ArrayUtil.isNotEmpty(client.getRedirectUri())) {
                        clientBuilder.redirectUris(client.getRedirectUri());
                    }
                    if (ArrayUtil.isNotEmpty(client.getScopes())) {
                        clientBuilder.redirectUris(client.getScopes());
                    }
                }
            }
        }
    }
}
