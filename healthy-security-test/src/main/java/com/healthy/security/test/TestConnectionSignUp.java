package com.healthy.security.test;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

//@Component
public class TestConnectionSignUp implements ConnectionSignUp {

    @Override
    public String execute(Connection<?> connection) {
        //根据社交用户信息默认创建用户并返回用户唯一标识
        return connection.getDisplayName();
    }
}
