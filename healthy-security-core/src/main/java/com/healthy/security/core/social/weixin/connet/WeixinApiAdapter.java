package com.healthy.security.core.social.weixin.connet;

import cn.hutool.core.util.StrUtil;
import com.healthy.security.core.social.weixin.api.Weixin;
import com.healthy.security.core.social.weixin.entity.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 微信 api适配器，将微信 api的数据模型转为spring social的标准模型。
 */
public class WeixinApiAdapter implements ApiAdapter<Weixin> {

    private String openId;

    public WeixinApiAdapter() {
    }

    public WeixinApiAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(Weixin api) {
        return true;
    }

    /**
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        WeixinUserInfo profile = api.getUserInfo(openId);
        values.setProviderUserId(profile.getOpenid());
        values.setDisplayName(profile.getNickname());
        values.setImageUrl(profile.getHeadimgurl());
        if (StrUtil.isNotBlank(profile.getUnionid())) {
            values.setProfileUrl(profile.getUnionid());
        }
    }

    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    @Override
    public void updateStatus(Weixin api, String message) {
    }

}
