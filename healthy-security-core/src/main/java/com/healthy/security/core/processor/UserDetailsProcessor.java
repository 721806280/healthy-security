package com.healthy.security.core.processor;

/**
 *  用户信息处理器
 * @author zhang.xiaoming
 */
public interface UserDetailsProcessor {

    /**
     * 效验是否当前处理器
     * @param   principal
     * @return  boolean
     */
    Boolean existProcessor(Object principal);

    /**
     * 获取用户信息
     * @param principal
     * @return  用户信息
     */
    String getPrincipal(Object principal);
}
