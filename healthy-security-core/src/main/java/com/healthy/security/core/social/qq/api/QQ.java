package com.healthy.security.core.social.qq.api;

import com.healthy.security.core.social.qq.entity.QQUserInfo;

import java.io.IOException;

public interface QQ {
    /**
     * 获取用户信息
     *
     * @return QQUserInfo
     * @throws IOException
     */
    QQUserInfo getUserInfo() throws IOException;
}