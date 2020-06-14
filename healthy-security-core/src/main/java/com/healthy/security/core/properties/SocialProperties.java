package com.healthy.security.core.properties;

import lombok.Data;

/**
 * 社交登录配置项
 */
@Data
public class SocialProperties {

    /**
     * 社交登录功能拦截的url
     */
    private String filterProcessesUrl = "/auth";

    /**
     * UserConnection 表前缀
     * 如设置 "healthy_" 即对应数据库表为"healthy_UserConnection"
     */
    private String jdbcTablePrefix;

    private QQProperties qq = new QQProperties();

    private WeixinProperties weixin = new WeixinProperties();

    private WeiboProperties weibo = new WeiboProperties();

    private WeixinMpProperties weixinmp = new WeixinMpProperties();

}
