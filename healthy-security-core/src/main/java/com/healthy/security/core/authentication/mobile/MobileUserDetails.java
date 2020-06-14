package com.healthy.security.core.authentication.mobile;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author zhang.xiaoming
 */
public interface MobileUserDetails extends UserDetails {
    /**
     * The user's identity at the provider.
     * Might be same as {@link #getUsername()} if users are identified by username
     * @return user's id used to assign connections
     */
    String getUserMobile();
}
