package com.healthy.security.core.social.weixinmp.connet;

import com.healthy.security.core.social.weixin.api.Weixin;
import com.healthy.security.core.social.weixin.connet.WeixinApiAdapter;
import com.healthy.security.core.social.weixinmp.api.WeixinMp;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 微信公众号连接工厂
 */
public class WeixinMpOAuth2ConnectionFactory extends OAuth2ConnectionFactory<WeixinMp> {

    /**
     * @param appId
     * @param appSecret
     */
    public WeixinMpOAuth2ConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeixinMpOAuth2ServiceProvider(appId, appSecret), new WeixinMpApiAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeixinMpAccessGrant) {
            return ((WeixinMpAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<WeixinMp> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<WeixinMp>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<WeixinMp> createConnection(ConnectionData data) {
        return new OAuth2Connection<WeixinMp>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<WeixinMp> getApiAdapter(String providerUserId) {
        return new WeixinMpApiAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<WeixinMp> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeixinMp>) getServiceProvider();
    }
}
