package com.healthy.security.core.properties;

import lombok.Data;

/**
 * 微信登录相关配置项
 */
@Data
public class WeixinMpProperties {

    /**
     * Application id.
     */
    private String appId;

    /**
     * Application secret.
     */
    private String appSecret;

    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 weixinmp。
     */
    private String providerId = "weixinmp";
}
