package com.leena.imageinsight.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int statusCode;
    private final String message;

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
}
