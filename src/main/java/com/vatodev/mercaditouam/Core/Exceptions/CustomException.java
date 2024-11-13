package com.vatodev.mercaditouam.Core.Exceptions;

public class CustomException extends RuntimeException {
    private final String errorCode;

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message) {
        super(message);
        this.errorCode = null;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
