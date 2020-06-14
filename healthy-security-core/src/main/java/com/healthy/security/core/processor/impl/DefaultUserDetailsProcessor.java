package com.healthy.security.core.processor.impl;

import com.healthy.security.core.processor.UserDetailsProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MAX_VALUE)
public class DefaultUserDetailsProcessor implements UserDetailsProcessor {
    @Override
    public Boolean existProcessor(Object principal) {
        return principal instanceof UserDetails;
    }

    @Override
    public String getPrincipal(Object principal) {
        return (String) principal;
    }
}
