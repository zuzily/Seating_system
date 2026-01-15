package com.example.seating_system.exception;

//業務例外
public class BusinessException extends RuntimeException {

    private static final String ERROR_CODE = "BUSINESS_ERROR";

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}