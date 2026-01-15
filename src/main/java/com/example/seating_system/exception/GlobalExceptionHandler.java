package com.example.seating_system.exception;

import com.example.seating_system.dto.response.ApiResponse;
import com.example.seating_system.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// 全域例外處理器
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 處理業務例外
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), Constants.ErrorCode.BUSINESS_ERROR));
    }

    // 處理資源未找到例外
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), Constants.ErrorCode.RESOURCE_NOT_FOUND));
    }

    // 處理座位已佔用例外
    @ExceptionHandler(SeatOccupiedException.class)
    public ResponseEntity<ApiResponse<Void>> handleSeatOccupiedException(SeatOccupiedException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), Constants.ErrorCode.SEAT_OCCUPIED));
    }

    // 處理驗證例外
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("驗證失敗", Constants.ErrorCode.VALIDATION_ERROR, errors));
    }

    // 處理所有其他例外
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        ex.printStackTrace(); // 記錄到 console
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("系統錯誤，請稍後再試", Constants.ErrorCode.SYSTEM_ERROR));
    }
}
