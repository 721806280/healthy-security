package com.healthy.security.server.handler;

import com.healthy.security.core.support.SimpleResponse;
import com.healthy.security.server.handler.exception.HealthyOauthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * healthy translator that converts exceptions into {@link OAuth2Exception}s. The output matches the OAuth 2.0
 * specification in terms of error response format and HTTP status code.
 *
 * @author zhang.xiaoming
 */
@Slf4j
@Component("healthyResponseExceptionTranslator")
public class HealthyResponseExceptionTranslator implements WebResponseExceptionTranslator<SimpleResponse> {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<SimpleResponse> translate(Exception e) throws Exception {

        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

        // 异常栈获取 OAuth2Exception 异常
        Exception exception = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);


        // 异常栈中有OAuth2Exception
        if (exception != null) {
            return handleOAuth2Exception((OAuth2Exception) exception);
        }

        exception = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);

        if (exception != null) {
            return handleOAuth2Exception(new HealthyOauthException(e.getMessage(), e));
        }

        exception = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (exception instanceof AccessDeniedException) {
            return handleOAuth2Exception(new HealthyOauthException(exception.getMessage(), exception));
        }

        exception = (HttpRequestMethodNotSupportedException) throwableAnalyzer
                .getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return handleOAuth2Exception(new HealthyOauthException(exception.getMessage(), exception));
        }

        exception = (UsernameNotFoundException) throwableAnalyzer
                .getFirstThrowableOfType(UsernameNotFoundException.class, causeChain);
        if (exception instanceof UsernameNotFoundException) {
            return handleOAuth2Exception(new HealthyOauthException(exception.getMessage(), exception));
        }

        // 不包含上述异常则服务器内部错误
        return handleOAuth2Exception(new HealthyOauthException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
    }

    private ResponseEntity<SimpleResponse> handleOAuth2Exception(OAuth2Exception e) throws IOException {

        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }

        SimpleResponse simpleResponse = new SimpleResponse(e.getMessage());

        return new ResponseEntity<SimpleResponse>(simpleResponse, headers, HttpStatus.valueOf(status));
    }
}
