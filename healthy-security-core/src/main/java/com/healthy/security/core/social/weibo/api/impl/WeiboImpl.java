package com.healthy.security.core.social.weibo.api.impl;

import cn.hutool.json.JSONUtil;
import com.healthy.security.core.social.weibo.api.Weibo;
import com.healthy.security.core.social.weibo.entity.Uid;
import com.healthy.security.core.social.weibo.entity.WeiboUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

@Slf4j
public class WeiboImpl extends AbstractOAuth2ApiBinding implements Weibo {
    /**
     * 获取 uid
     * https://open.weibo.com/wiki/2/account/get_uid
     */
    private final static String URL_GET_OPENID = "https://api.weibo.com/2/account/get_uid.json";
    /**
     * 获取用户信息
     * https://api.weibo.com/2/users/show.json
     */
    private final static String URL_GET_USERINFO = "https://api.weibo.com/2/users/show.json?uid=%s";

    private String openId;

    public WeiboImpl(String accessToken) {
        // TokenStrategy.ACCESS_TOKEN_PARAMETER策略会携带access_token参数作为查询条件
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.openId = this.getUid().getUid();
    }

    @Override
    public Uid getUid() {
        String result = getRestTemplate().getForObject(URL_GET_OPENID, String.class);
        log.debug("获取uid响应：{}", result);
        Uid uid = JSONUtil.toBean(result, Uid.class);
        return uid;
    }

    @Override
    public WeiboUserInfo getUserInfo() {
        String url = String.format(URL_GET_USERINFO, openId);
        String result = getRestTemplate().getForObject(url, String.class);
        log.debug("UserInfo：{}", result);
        WeiboUserInfo userInfo = JSONUtil.toBean(result, WeiboUserInfo.class);
        return userInfo;
    }
}