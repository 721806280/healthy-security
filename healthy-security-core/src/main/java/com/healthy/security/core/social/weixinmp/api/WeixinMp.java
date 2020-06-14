package com.healthy.security.core.social.weixinmp.api;

import com.healthy.security.core.social.weixinmp.entity.WeixinMpUserInfo;

/**
 * 微信公众号 API调用接口
 */
public interface WeixinMp {
    WeixinMpUserInfo getUserInfo(String openId);
}
