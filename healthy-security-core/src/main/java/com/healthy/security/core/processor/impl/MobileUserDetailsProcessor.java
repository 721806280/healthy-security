package com.healthy.security.core.processor.impl;

import com.healthy.security.core.authentication.mobile.MobileUserDetails;
import com.healthy.security.core.processor.UserDetailsProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class MobileUserDetailsProcessor implements UserDetailsProcessor {
    @Override
    public Boolean existProcessor(Object principal) {
        return principal instanceof MobileUserDetails;
    }

    @Override
    public String getPrincipal(Object principal) {
        return ((MobileUserDetails) principal).getUserMobile();
    }
}
