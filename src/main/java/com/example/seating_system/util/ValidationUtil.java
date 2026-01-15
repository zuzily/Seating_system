package com.example.seating_system.util;

import com.example.seating_system.dto.request.AssignSeatRequest;
import com.example.seating_system.dto.request.BatchUpdateRequest;
import com.example.seating_system.exception.BusinessException;

import java.util.regex.Pattern;

// 驗證工具類別
public class ValidationUtil {

    // 員工編號路徑常數
    public static final String EMPLOYEES_PATH = "/api/employees";

    // 錯誤訊息常數
    public static final String ERROR_SQL_INJECTION = "偵測到潛在的 SQL 注入攻擊";
    public static final String MSG_SQL_INJECTION = "輸入包含非法字元";
    public static final String ERROR_INVALID_EMP_ID = "員工編號格式錯誤";
    public static final String MSG_INVALID_EMP_ID = "員工編號必須為 5 位數字";
    public static final String ERROR_XSS_DETECTED = "偵測到潛在的 XSS 攻擊";
    public static final String MSG_XSS_DETECTED = "輸入包含非法腳本";

    // 正則表達式模式
    private static final Pattern EMP_ID_PATTERN = Pattern.compile(Constants.Regex.EMP_ID);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(Constants.Regex.EMAIL);
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "('.*(\\b(SELECT|UNION|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|EXEC|EXECUTE)\\b).*')|" +
                    "(--)|" +
                    "(/\\*.*\\*/)|" +
                    "(;.*--)"
            , Pattern.CASE_INSENSITIVE);
    private static final Pattern XSS_PATTERN = Pattern.compile(
            "(<script[^>]*>.*?</script>)|" +
                    "(<.*?javascript:.*?>)|" +
                    "(<.*?on\\w+\\s*=.*?>)"
            , Pattern.CASE_INSENSITIVE);

    // 驗證員工編號格式
    public static void validateEmpId(String empId) {
        if (empId == null || empId.trim().isEmpty()) {
            throw new BusinessException(Constants.ValidationMessage.EMP_ID_REQUIRED);
        }

        if (!EMP_ID_PATTERN.matcher(empId).matches()) {
            throw new BusinessException(Constants.ValidationMessage.EMP_ID_INVALID);
        }
    }

    // 驗證電子郵件格式
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException(Constants.ValidationMessage.EMAIL_REQUIRED);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(Constants.ValidationMessage.EMAIL_INVALID);
        }
    }

    // 驗證座位分配請求
    public static void validateAssignSeatRequest(AssignSeatRequest request) {
        if (request == null) {
            throw new BusinessException("請求參數不能為空");
        }

        validateEmpId(request.getEmpId());

        if (request.getFloorSeatSeq() == null) {
            throw new BusinessException(Constants.ValidationMessage.FLOOR_SEAT_SEQ_REQUIRED);
        }

        if (request.getFloorSeatSeq() <= 0) {
            throw new BusinessException("座位序號必須大於 0");
        }
    }

    // 驗證批次更新請求
    public static void validateBatchUpdateRequest(BatchUpdateRequest request) {
        if (request == null) {
            throw new BusinessException("請求參數不能為空");
        }

        if (request.getAssignments() == null || request.getAssignments().isEmpty()) {
            throw new BusinessException(Constants.ValidationMessage.ASSIGNMENTS_REQUIRED);
        }

        // 驗證每個分配請求
        for (AssignSeatRequest assignment : request.getAssignments()) {
            validateAssignSeatRequest(assignment);
        }
    }

    // 檢查 SQL 注入
    public static boolean containsSqlInjection(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return SQL_INJECTION_PATTERN.matcher(input).find();
    }

    // 檢查 XSS 攻擊
    public static boolean containsXss(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return XSS_PATTERN.matcher(input).find();
    }

    // 驗證輸入字串的安全性
    public static void validateInputSecurity(String input, String fieldName) {
        if (input == null) {
            return;
        }

        if (containsSqlInjection(input)) {
            throw new BusinessException(
                    String.format("%s 包含非法字元（SQL注入）", fieldName)
            );
        }

        if (containsXss(input)) {
            throw new BusinessException(
                    String.format("%s 包含非法字元（XSS攻擊）", fieldName)
            );
        }
    }

    // 驗證樓層編號
    public static void validateFloorNo(Integer floorNo) {
        if (floorNo == null) {
            throw new BusinessException(Constants.ValidationMessage.FLOOR_NO_REQUIRED);
        }

        if (floorNo <= 0) {
            throw new BusinessException("樓層編號必須大於 0");
        }
    }

    // 驗證座位序號
    public static void validateFloorSeatSeq(Integer floorSeatSeq) {
        if (floorSeatSeq == null) {
            throw new BusinessException(Constants.ValidationMessage.FLOOR_SEAT_SEQ_REQUIRED);
        }

        if (floorSeatSeq <= 0) {
            throw new BusinessException("座位序號必須大於 0");
        }
    }

    // 驗證姓名
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(Constants.ValidationMessage.NAME_REQUIRED);
        }

        if (name.trim().length() > 100) {
            throw new BusinessException("姓名長度不能超過 100 字元");
        }

        validateInputSecurity(name, "姓名");
    }

    // 驗證分頁參數
    public static void validatePagination(Integer page, Integer size) {
        if (page != null && page < 0) {
            throw new BusinessException("頁碼不能小於 0");
        }

        if (size != null) {
            if (size <= 0) {
                throw new BusinessException("每頁數量必須大於 0");
            }
            if (size > Constants.Pagination.MAX_PAGE_SIZE) {
                throw new BusinessException(
                        String.format("每頁數量不能超過 %d", Constants.Pagination.MAX_PAGE_SIZE)
                );
            }
        }
    }

    // 私有建構子，防止實例化
    private ValidationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}