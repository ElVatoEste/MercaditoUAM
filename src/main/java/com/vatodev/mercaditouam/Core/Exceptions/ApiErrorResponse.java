package com.vatodev.mercaditouam.Core.Exceptions;

public class ApiErrorResponse {
    private int status;
    private String message;
    private String errorCode;

    public ApiErrorResponse(int status, String message, String errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters y Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
