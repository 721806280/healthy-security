package com.healthy.security.core.social.weibo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WeiboUserInfo {
    /**
     * 是否允许所有人给我发私信
     */
    private boolean allowAllActMsg;
    /**
     * 是否允许所有人对我的微博进行评论
     */
    private boolean allowAllComment;
    /**
     * 小头像
     */
    private String profileImageUrl;
    /**
     * 标准头像
     */
    private String avatarLarge;
    /**
     * 高清头像
     */
    private String avatarHd;
    /**
     * 互粉数
     */
    private int biFollowersCount;
    /**
     * 城市编码（参考城市编码表）
     */
    private int city;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 个人描述
     */
    private String description;
    /**
     * 收藏数
     */
    private int favouritesCount;
    /**
     * 粉丝数
     */
    private int followersCount;
    /**
     * 保留字段,是否已关注(此特性暂不支持)
     */
    private boolean following;
    /**
     * 此用户是否关注我
     */
    private boolean followMe;
    /**
     * 关注数
     */
    private int friendsCount;
    /**
     * 性别,m--男，f--女,n--未知
     */
    private String gender;
    /**
     * 用户UID
     */
    private Long id;
    /**
     * 用户UID
     */
    private String idstr;
    /**
     * 地址
     */
    private String location;
    /**
     * 友好显示名称，如Bill Gates,名称中间的空格正常显示(此特性暂不支持)
     */
    private String name;
    /**
     * 用户在线状态
     */
    private int onlineStatus;
    /**
     * 省份编码（参考省份编码表）
     */
    private int province;
    /**
     * 微博昵称
     */
    private String screenName;
    /**
     * 用户最新一条微博
     */
    private Status status = null;
    /**
     * 微博数
     */
    private int statusesCount;
    /**
     * 用户博客地址
     */
    private String url;
    /**
     * 用户的微博统一URL地址
     */
    private String profileUrl;
    /**
     * 用户个性化URL
     */
    private String domain;
    /**
     * 加V标示，是否微博认证用户
     */
    private boolean verified;
    /**
     * 认证原因
     */
    private String verifiedReason;
}
