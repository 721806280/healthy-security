package com.healthy.security.server;

import com.healthy.security.core.properties.SecurityProperties;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Map;

/**
 * 令牌增强器(个性化token)
 * 用于向jwt中添加额外信息
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    private final SecurityProperties securityProperties;

    public JwtTokenEnhancer(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> info = defaultOAuth2AccessToken.getAdditionalInformation();
        info.put("jwtSigningKey", securityProperties.getOauth2().getJwtSigningKey());
        defaultOAuth2AccessToken.setAdditionalInformation(info);
        return defaultOAuth2AccessToken;
    }
}