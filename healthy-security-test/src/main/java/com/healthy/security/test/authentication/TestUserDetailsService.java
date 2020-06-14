package com.healthy.security.test.authentication;

import com.healthy.security.core.authentication.mobile.MobileUser;
import com.healthy.security.core.authentication.mobile.MobileUserDetails;
import com.healthy.security.core.authentication.mobile.MobileUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component("userDetailsService")
public class TestUserDetailsService implements UserDetailsService, MobileUserDetailsService, SocialUserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户名:{}", username);
        return buildSocialUser(username);
    }

    @Override
    public MobileUserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        log.info("登录手机号:{}", mobile);

        String password = passwordEncoder.encode("123456");
        log.info("数据库密码{}", password);
        return new MobileUser(mobile,
                password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        log.info("登录用户名:{}", userId);
        return buildSocialUser(userId);
    }

    private SocialUserDetails buildSocialUser(String username) {
        String password = passwordEncoder.encode("123456");
        log.info("数据库密码{}", password);
        SocialUser admin = new SocialUser(username,
                password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
        return admin;
    }
}