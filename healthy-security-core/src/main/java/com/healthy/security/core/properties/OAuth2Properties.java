package com.healthy.security.core.properties;

import lombok.Data;

/**
 *  OAuth2基本数据配置项
 */
@Data
public class OAuth2Properties {

    /**
     * 使用jwt时为token签名的秘钥
     */
    private String jwtSigningKey = "healthy";

    /**
     * clientDetail存储策略，默认none，可选值：jdbc(数据库存储)，none(内存存储)
     */
    private ClientDetailStoreType clientDetailStoreType = ClientDetailStoreType.NONE;

    /**
     * 客户端配置
     */
    private OAuth2ClientProperties[] clients = {};
}


