package com.healthy.security.core.social.qq.connet;

import com.healthy.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * QQ连接工厂
 * 参考GenericOAuth2ConnectionFactory
 */
public class QQOAuth2ConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    /**
     * 唯一的构造函数，需要
     * Create a {@link OAuth2ConnectionFactory}.
     *
     * @param providerId 服务商id；自定义字符串；也是后面添加social的过滤，过滤器帮我们拦截的url其中的某一段地址
     *                   on} interface.
     */
    public QQOAuth2ConnectionFactory(String providerId, String appid, String secret) {
        /**
         * serviceProvider 用于执行授权流和获取本机服务API实例的ServiceProvider模型
         * apiAdapter      适配器，用于将不同服务提供商的个性化用户信息映射到 {@link Connection}
         */
        super(providerId, new QQOAuth2ServiceProvider(appid, secret), new QQApiAdapter());
    }
}