package com.healthy.security.core.social.qq.connet;

import com.healthy.security.core.social.qq.api.QQ;
import com.healthy.security.core.social.qq.entity.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import java.io.IOException;

/**
 * 适配器，用于将不同服务提供商的个性化用户信息映射到 {@link Connection}
 */
public class QQApiAdapter implements ApiAdapter<QQ> {
    @Override
    public boolean test(QQ api) {
        // 测试服务是否可用
        return true;
    }

    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = null;
        try {
            userInfo = api.getUserInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        // 主页地址，像微博一般有主页地址
        values.setProfileUrl(null);
        // 服务提供商返回的该user的openid 一般来说这个openid是和你的开发账户也就是appid绑定的
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return UserProfile.EMPTY;
    }

    @Override
    public void updateStatus(QQ api, String message) {
    }
}