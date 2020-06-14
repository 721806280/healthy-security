package com.healthy.security.core.social.qq.api.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.healthy.security.core.social.qq.api.QQ;
import com.healthy.security.core.social.qq.entity.ClientOpenId;
import com.healthy.security.core.social.qq.entity.QQUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {
    /**
     * 获取 OPENID
     * http://wiki.connect.qq.com/%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7openid_oauth2-0
     */
    public final static String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    /**
     * 获取用户信息
     * TokenStrategy.ACCESS_TOKEN_PARAMETER策略会携带access_token参数作为查询条件
     */
    public final static String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId;

    private String openId;

    public QQImpl(String accessToken, String appId) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;
        String url = String.format(URL_GET_OPENID, accessToken);
        //callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
        String result = getRestTemplate().getForObject(url, String.class);
        log.debug("获取openid响应：{}", result);
        String resultJsonStr = StrUtil.subBetween(result, "(", ")");
        ClientOpenId clientOpenId = JSONUtil.toBean(resultJsonStr, ClientOpenId.class);
        this.openId = clientOpenId.getOpenid();
    }

    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(URL_GET_USERINFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);
        log.debug("UserInfo：{}", result);
        QQUserInfo userInfo = JSONUtil.toBean(result, QQUserInfo.class);
        userInfo.setOpenId(openId);
        return userInfo;
    }
}