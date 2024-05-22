package com.demo.travellybe.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final String code;
    private final String message;

    public AuthException(final ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}