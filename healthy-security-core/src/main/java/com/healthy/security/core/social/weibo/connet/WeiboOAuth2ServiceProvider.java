package com.healthy.security.core.social.weibo.connet;

import com.healthy.security.core.social.weibo.api.Weibo;
import com.healthy.security.core.social.weibo.api.impl.WeiboImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class WeiboOAuth2ServiceProvider extends AbstractOAuth2ServiceProvider<Weibo> {

    private static final String URL_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";
    private static final String URL_ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";

    public WeiboOAuth2ServiceProvider(String appId, String appSecret) {
        super(new WeiboOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
    }

    @Override
    public Weibo getApi(String accessToken) {
        return new WeiboImpl(accessToken);
    }
}
