package com.healthy.security.core.social.weixinmp.connet;

import com.healthy.security.core.social.weixinmp.api.WeixinMp;
import com.healthy.security.core.social.weixinmp.api.impl.WeixinMpImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 微信公众号的OAuth2流程处理器的提供器，供spring social的connect体系调用
 * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html
 */
public class WeixinMpOAuth2ServiceProvider extends AbstractOAuth2ServiceProvider<WeixinMp> {

    /**
     * 微信公众号获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";
    /**
     * 微信公众号获取accessToken的url
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * @param appId
     * @param appSecret
     */
    public WeixinMpOAuth2ServiceProvider(String appId, String appSecret) {
        super(new WeixinMpOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
    }

    /**
     * @param accessToken
     * @return
     * @see AbstractOAuth2ServiceProvider#getApi(String)
     */
    @Override
    public WeixinMp getApi(String accessToken) {
        return new WeixinMpImpl(accessToken);
    }

}
