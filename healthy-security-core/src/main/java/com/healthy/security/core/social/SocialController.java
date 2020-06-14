package com.healthy.security.core.social;

import com.healthy.security.core.social.support.SocialUserInfo;
import org.springframework.social.connect.Connection;

import java.util.Objects;

public abstract class SocialController {
    /**
     * 根据Connection信息构建SocialUserInfo
     *
     * @param connection
     * @return
     */
    protected SocialUserInfo buildSocialUserInfo(Connection<?> connection) {
        SocialUserInfo userInfo = null;
        if (!Objects.isNull(connection)) {
            userInfo = new SocialUserInfo();
            userInfo.setProviderId(connection.getKey().getProviderId());
            userInfo.setProviderUserId(connection.getKey().getProviderUserId());
            userInfo.setNickname(connection.getDisplayName());
            userInfo.setHeadimg(connection.getImageUrl());
        }
        return userInfo;
    }
}
