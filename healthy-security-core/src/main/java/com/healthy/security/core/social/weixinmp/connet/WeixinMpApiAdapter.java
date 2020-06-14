package com.healthy.security.core.social.weixinmp.connet;

import cn.hutool.core.util.StrUtil;
import com.healthy.security.core.social.weixin.api.Weixin;
import com.healthy.security.core.social.weixin.entity.WeixinUserInfo;
import com.healthy.security.core.social.weixinmp.api.WeixinMp;
import com.healthy.security.core.social.weixinmp.entity.WeixinMpUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 微信公众号 api适配器，将微信公众号 api的数据模型转为spring social的标准模型。
 */
public class WeixinMpApiAdapter implements ApiAdapter<WeixinMp> {

    private String openId;

    public WeixinMpApiAdapter() {
    }

    public WeixinMpApiAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(WeixinMp api) {
        return true;
    }

    /**
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(WeixinMp api, ConnectionValues values) {
        WeixinMpUserInfo profile = api.getUserInfo(openId);
        values.setProviderUserId(profile.getOpenid());
        values.setDisplayName(profile.getNickname());
        values.setImageUrl(profile.getHeadimgurl());
        if (StrUtil.isNotBlank(profile.getUnionid())) {
            values.setProfileUrl(profile.getUnionid());
        }
    }

    @Override
    public UserProfile fetchUserProfile(WeixinMp api) {
        return null;
    }

    @Override
    public void updateStatus(WeixinMp api, String message) {
    }

}
