package com.healthy.security.core.authentication;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.healthy.security.core.processor.UserDetailsProcessorHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
 * 日志记录
 */
@Slf4j
public class HealthyAuditLogFilter extends OncePerRequestFilter {

    private final UserDetailsProcessorHolder userDetailsProcessorHolder;

    public HealthyAuditLogFilter(UserDetailsProcessorHolder userDetailsProcessorHolder) {
        this.userDetailsProcessorHolder = userDetailsProcessorHolder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication instanceof AnonymousAuthenticationToken ? authentication.getPrincipal().toString() : userDetailsProcessorHolder.findUserDetailsPrincipal(authentication.getPrincipal());

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        TimeInterval timer = DateUtil.timer();

        filterChain.doFilter(requestWrapper, responseWrapper);

        auditLog(userName, timer, requestWrapper, responseWrapper);
    }

    private void auditLog(String userName, TimeInterval timer, ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) throws IOException, ServletException {
        String responsePayload = StrUtil.str(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        responseWrapper.copyBodyToResponse();
        String clientIp = ServletUtil.getClientIP(requestWrapper);
        String remoteAddr = StrUtil.equals(clientIp, "0:0:0:0:0:0:0:1") ? "127.0.0.1" : clientIp;
        log.debug("IP: [{}]  Method: [{}]  URL: [{}]", remoteAddr, requestWrapper.getMethod(), requestWrapper.getRequestURI());
        Map<String, String> paramMap = ServletUtil.getParamMap(requestWrapper);
        if (MapUtil.isNotEmpty(paramMap)) {
            log.debug("请求参数: [{}]", JSONUtil.toJsonStr(paramMap));
        }
        String requestPayload = StrUtil.str(requestWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        if (StrUtil.isNotEmpty(requestPayload)) {
            log.debug("请求体: [{}]", StrUtil.cleanBlank(StrUtil.removeAllLineBreaks(requestPayload)));
        }
        log.debug("用户: [{}]", userName);
        log.debug("耗时: [{}(ms)]", timer.intervalRestart());
        log.debug("状态: [{}]", responseWrapper.getStatus());
        if (StrUtil.isNotBlank(responsePayload)) {
            log.debug("响应: [{}]", StrUtil.cleanBlank(StrUtil.removeAllLineBreaks(responsePayload)));
        }
    }
}
