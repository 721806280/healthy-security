package com.healthy.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码存取器
 */
public interface ValidateCodeRepository {

    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 保存验证码
     *
     * @param request
     * @param code
     * @param validateCodeType
     */
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

    /**
     * 获取验证码
     *
     * @param request
     * @param validateCodeType
     * @return
     */
    ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

    /**
     * 移除验证码
     *
     * @param request
     * @param codeType
     */
    void remove(ServletWebRequest request, ValidateCodeType codeType);

}
