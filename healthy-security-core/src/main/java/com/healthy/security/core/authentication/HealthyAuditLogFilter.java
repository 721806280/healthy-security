package com.healthy.security.core.authentication;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.healthy.security.core.properties.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * HealthyAuditLogFilter
 *
 * @author xiaomingzhang
 */
@Slf4j
public class HealthyAuditLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        TimeInterval timer = DateUtil.timer();
        filterChain.doFilter(requestWrapper, responseWrapper);
        auditLog(authentication.getName(), timer, requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();
    }

    private void auditLog(String userName, TimeInterval timer, ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) throws IOException {
        String contentType = responseWrapper.getContentType();
        String authorization = requestWrapper.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotBlank(authorization)) {
            log.debug("Authorization : [{}]", authorization);
        }

        log.debug("IP            : [{}]", ServletUtil.getClientIP(requestWrapper));
        log.debug("URI           : [{}]", requestWrapper.getRequestURI());
        log.debug("Method        : [{}]", requestWrapper.getMethod());
        log.debug("ContentType   : [{}]", contentType);
        log.debug("User          : [{}]", userName);
        log.debug("Status        : [{}]", responseWrapper.getStatus());
        log.debug("TimeCon       : [{}/ms]", timer.intervalRestart());

        Map<String, String> paramMap = ServletUtil.getParamMap(requestWrapper);
        if (MapUtil.isNotEmpty(paramMap)) {
            log.debug("RequestParam  : [{}]", JSONUtil.toJsonStr(paramMap));
        }

        String requestPayload = StrUtil.str(requestWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        if (StrUtil.isNotBlank(requestPayload)) {
            log.debug("RequestBody   : [{}]", StrUtil.cleanBlank(StrUtil.removeAllLineBreaks(requestPayload)));
        }

        String responsePayload = StrUtil.str(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        if (StrUtil.isNotBlank(responsePayload) && StrUtil.equalsAny(contentType, SecurityConstants.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE)) {
            log.debug("ResponseBody  : [{}]", responsePayload);
        }
    }
}