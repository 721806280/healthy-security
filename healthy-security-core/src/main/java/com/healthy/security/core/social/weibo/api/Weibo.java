package com.healthy.security.core.social.weibo.api;

import com.healthy.security.core.social.weibo.entity.Uid;
import com.healthy.security.core.social.weibo.entity.WeiboUserInfo;

public interface Weibo {
    /**
     * 获取用户Uid
     * @return
     */
    Uid getUid();

    /**
     * 获取用户信息
     *
     * @return
     */
    WeiboUserInfo getUserInfo();
}