package com.example.seating_system.exception;

// 座位已佔用例外
public class SeatOccupiedException extends RuntimeException {

    private static final String ERROR_CODE = "SEAT_OCCUPIED";

    public SeatOccupiedException(String message) {
        super(message);
    }

    public SeatOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}