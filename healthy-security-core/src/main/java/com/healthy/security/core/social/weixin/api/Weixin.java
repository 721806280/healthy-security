package com.healthy.security.core.social.weixin.api;

import com.healthy.security.core.social.weixin.entity.WeixinUserInfo;

/**
 * 微信API调用接口
 */
public interface Weixin {
    WeixinUserInfo getUserInfo(String openId);
}
