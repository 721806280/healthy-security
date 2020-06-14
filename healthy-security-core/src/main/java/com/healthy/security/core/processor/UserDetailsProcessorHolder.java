package com.healthy.security.core.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 用户信息处理器管理器
 * @author zhang.xiaoming
 */
@Component
public class UserDetailsProcessorHolder {

    @Autowired
    private List<UserDetailsProcessor> userDetailsProcessors;

    /**
     * @param principal
     * @return
     */
    public UserDetailsProcessor findUserDetailsProcessor(Object principal) {
        for (UserDetailsProcessor userDetailsProcessor : userDetailsProcessors) {
            if (userDetailsProcessor.existProcessor(principal)) {
                return userDetailsProcessor;
            }
        }
        return null;
    }

    public String findUserDetailsPrincipal(Object principal) {
        UserDetailsProcessor userDetailsProcessor = findUserDetailsProcessor(principal);
        String principalName = "";
        if (!Objects.isNull(userDetailsProcessor)) {
            principalName = userDetailsProcessor.getPrincipal(principal);
        }
        return principalName;
    }
}
