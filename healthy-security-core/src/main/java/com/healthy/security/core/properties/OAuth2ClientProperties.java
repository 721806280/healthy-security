package com.healthy.security.core.properties;

import lombok.Data;

/**
 * 认证服务器注册的第三方应用配置项
 */
@Data
public class OAuth2ClientProperties {
    /**
     * 第三方应用appId
     */
    private String clientId;
    /**
     * 第三方应用appSecret
     */
    private String clientSecret;
    /**
     * authorizedGrantTypes
     */
    private String[] authorizedGrantTypes = new String[]{"password"};
    /**
     * 针对此应用发出的token的有效时间
     */
    private int accessTokenValidateSeconds = 7200;
    /**
     * 针对此应用发出的refreshToken的有效时间
     */
    private int refreshTokenValiditySeconds = 7200;
    /**
     * 重定向的url
     */
    private String[] redirectUri;
    /**
     * scopes
     */
    private String[] scopes;
}
