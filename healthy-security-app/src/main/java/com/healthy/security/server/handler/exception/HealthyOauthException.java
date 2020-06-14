package com.healthy.security.server.handler.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class HealthyOauthException extends OAuth2Exception {

    public HealthyOauthException(String msg, Throwable t) {
        super(msg, t);
    }

    public HealthyOauthException(String msg) {
        super(msg);
    }
}
