package com.healthy.security.app;

public class AppSecretException extends RuntimeException {

    private static final long serialVersionUID = -1629364510827838114L;

    public AppSecretException(String msg) {
        super(msg);
    }

}
