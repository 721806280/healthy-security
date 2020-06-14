package com.healthy.security.core.social.weibo.connet;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.healthy.security.core.social.weibo.api.Weibo;
import com.healthy.security.core.social.weibo.entity.WeiboUserInfo;
import org.springframework.social.connect.*;

import java.util.Objects;

/**
 * 适配器，用于将不同服务提供商的个性化用户信息映射到 {@link Connection}
 */
public class WeiboApiAdapter implements ApiAdapter<Weibo> {

    private final String profileUrlPrefix = "http://weibo.com/{}";

    @Override
    public boolean test(Weibo api) {
        // 测试服务是否可用
        return true;
    }

    @Override
    public void setConnectionValues(Weibo api, ConnectionValues values) {
        WeiboUserInfo userInfo = api.getUserInfo();
        values.setDisplayName(userInfo.getName());
        values.setImageUrl(userInfo.getAvatarLarge());
        values.setProfileUrl(StrFormatter.format(profileUrlPrefix, userInfo.getProfileUrl()));
        // 服务提供商返回的该user的openid 一般来说这个openid是和你的开发账户也就是appid绑定的
        values.setProviderUserId(userInfo.getIdstr());
    }

    @Override
    public UserProfile fetchUserProfile(Weibo api) {
        WeiboUserInfo weiboUserInfo = api.getUserInfo();
        if (!Objects.isNull(weiboUserInfo)) {
            String name = weiboUserInfo.getName();
            return new UserProfileBuilder()
                    .setUsername(weiboUserInfo.getScreenName()).setName(name)
                    .setLastName(extractChineseLastName(name))
                    .setFirstName(extractChineseFirstname(name)).build();
        }
        return UserProfile.EMPTY;
    }

    @Override
    public void updateStatus(Weibo api, String message) {
    }

    private String extractChineseFirstname(String name) {
        String result = null;
        if (StrUtil.isNotBlank(name)) {
            result = name.substring(1);
        }
        return result;
    }

    private String extractChineseLastName(String name) {
        String result = null;
        if (StrUtil.isNotBlank(name)) {
            result = name.substring(0, 1);
        }
        return result;
    }
}