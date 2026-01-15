package com.example.seating_system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

// 統一 API 回應格式
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    // 回應狀態
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_ERROR = "error";
    private static final String STATUS_PARTIAL_SUCCESS = "partial_success";

    private String status;
    private String message;
    private String code;
    private T data;
    private LocalDateTime timestamp;

    // 建構子
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(String status, String message, String code, T data) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(STATUS_SUCCESS, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(STATUS_SUCCESS, message, null);
    }

    public static <T> ApiResponse<T> error(String message, String code, T data) {
        return new ApiResponse<>(STATUS_ERROR, message, code, data);
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(STATUS_ERROR, message, code, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(STATUS_ERROR, message, null, null);
    }

    public static <T> ApiResponse<T> partialSuccess(String message, T data) {
        return new ApiResponse<>(STATUS_PARTIAL_SUCCESS, message, data);
    }

    // Getter 和 Setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
