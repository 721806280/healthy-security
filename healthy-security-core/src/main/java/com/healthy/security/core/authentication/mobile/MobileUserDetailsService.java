package com.healthy.security.core.authentication.mobile;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;

/**
 * similar to {@link UserDetailsService} but loads details by user id, not username
 * @author Stefan Fussennegger
 */
public interface MobileUserDetailsService {

    /**
     * @param mobile the user ID used to lookup the user details
     * @return the SocialUserDetails requested
     * @see UserDetailsService#loadUserByUsername(String)
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;
}
