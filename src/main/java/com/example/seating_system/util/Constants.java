package com.example.seating_system.util;

// 常數定義類別
public class Constants {

    // 座位狀態
    public static class SeatStatus {
        public static final String AVAILABLE = "available";   // 空位
        public static final String OCCUPIED = "occupied";     // 已佔用
    }

    // 操作結果代碼
    public static class ResultCode {
        public static final int SUCCESS = 1;           // 成功
        public static final int FAILURE = 0;           // 失敗
        public static final int ERROR = -1;            // 系統錯誤
    }

    // 錯誤代碼
    public static class ErrorCode {
        public static final String BUSINESS_ERROR = "BUSINESS_ERROR";
        public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
        public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
        public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
        public static final String SEAT_OCCUPIED = "SEAT_OCCUPIED";
        public static final String EMPLOYEE_HAS_SEAT = "EMPLOYEE_HAS_SEAT";
        public static final String ALREADY_EXISTS = "ALREADY_EXISTS";
        public static final String NOT_FOUND = "NOT_FOUND";
    }

    // 回應狀態
    public static class ResponseStatus {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String PARTIAL_SUCCESS = "partial_success";
    }

    // 驗證訊息
    public static class ValidationMessage {
        public static final String EMP_ID_REQUIRED = "員工編號不能為空";
        public static final String EMP_ID_INVALID = "員工編號格式錯誤";
        public static final String FLOOR_SEAT_SEQ_REQUIRED = "座位序號不能為空";
        public static final String FLOOR_NO_REQUIRED = "樓層編號不能為空";
        public static final String ASSIGNMENTS_REQUIRED = "分配清單不能為空";
        public static final String NAME_REQUIRED = "姓名不能為空";
        public static final String EMAIL_REQUIRED = "電子郵件不能為空";
        public static final String EMAIL_INVALID = "電子郵件格式錯誤";
    }

    // 業務訊息
    public static class BusinessMessage {
        public static final String SEAT_NOT_FOUND = "座位不存在";
        public static final String SEAT_OCCUPIED = "座位已被佔用";
        public static final String EMPLOYEE_NOT_FOUND = "員工不存在";
        public static final String EMPLOYEE_HAS_SEAT = "員工已有座位";
        public static final String SEAT_ASSIGN_SUCCESS = "座位分配成功";
        public static final String SEAT_CLEAR_SUCCESS = "座位清除成功";
        public static final String OPERATION_SUCCESS = "操作成功";
        public static final String OPERATION_FAILED = "操作失敗";
    }

    // 正則表達式
    public static class Regex {
        // 員工編號：5 位數字
        public static final String EMP_ID = "^\\d{5}$";
        // 電子郵件
        public static final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    }

    // 分頁設定
    public static class Pagination {
        public static final int DEFAULT_PAGE_SIZE = 20;
        public static final int MAX_PAGE_SIZE = 100;
        public static final int DEFAULT_PAGE_NUMBER = 1;
    }

    // 資料庫設定
    public static class Database {
        public static final String PROCEDURE_ASSIGN_SEAT = "SP_ASSIGN_SEAT";
        public static final String PROCEDURE_CLEAR_SEAT = "SP_CLEAR_SEAT";
        public static final String PROCEDURE_GET_ALL_SEATS = "SP_GET_ALL_SEATS";
        public static final String PROCEDURE_GET_ALL_EMPLOYEES = "SP_GET_ALL_EMPLOYEES";
    }

    // 私有建構子，防止實例化
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}