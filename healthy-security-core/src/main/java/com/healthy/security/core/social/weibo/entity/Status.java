package com.healthy.security.core.social.weibo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Status {
    private Long id;
    private Date createdAt;
    private String text;
    private String source;
    private boolean favorited;
    private boolean truncated;
    private String inReplyToStatusId;
    private String inReplyToUserId;
    private String inReplyToScreenName;
    private String mid;
    private WeiboUserInfo user;
    private int repostsCount;
    private int commentsCount;
    private Status originalStatus;
}
