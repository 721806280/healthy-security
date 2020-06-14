package com.healthy.security.core.processor.impl;

import com.healthy.security.core.processor.UserDetailsProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SocialUserDetailsProcessor implements UserDetailsProcessor {
    @Override
    public Boolean existProcessor(Object principal) {
        return principal instanceof SocialUserDetails;
    }

    @Override
    public String getPrincipal(Object principal) {
        return ((SocialUserDetails) principal).getUserId();
    }
}
