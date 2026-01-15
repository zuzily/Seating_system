package com.example.seating_system.util;

import java.util.regex.Pattern;

// 安全工具類別
public class SecurityUtil {

    // SQL 注入關鍵字
    private static final String[] SQL_INJECTION_KEYWORDS = {
            "SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "CREATE", "ALTER",
            "EXEC", "EXECUTE", "UNION", "SCRIPT", "JAVASCRIPT", "EVAL",
            "--", "/*", "*/", "@@", "@", "CHAR", "NCHAR", "VARCHAR",
            "NVARCHAR", "ALTER", "BEGIN", "CAST", "CURSOR", "DECLARE",
            "EXEC", "EXECUTE", "FETCH", "KILL", "OPEN", "SYS", "SYSOBJECTS",
            "SYSCOLUMNS", "TABLE", "XTYPE"
    };

    // XSS 攻擊模式
    private static final Pattern[] XSS_PATTERNS = {
            Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<iframe[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<object[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<embed[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<img[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("eval\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("expression\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE)
    };

    // XSS 轉義字元對應
    private static final String[][] XSS_ESCAPE_CHARS = {
            {"<", "&lt;"},
            {">", "&gt;"},
            {"\"", "&quot;"},
            {"'", "&#x27;"},
            {"/", "&#x2F;"},
            {"&", "&amp;"}
    };

    // 員工編號格式 (5位數字)
    private static final Pattern EMP_ID_PATTERN = Pattern.compile("^\\d{5}$");

    // 清理 XSS 攻擊字串
    public static String sanitizeXSS(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String sanitized = input;

        // 移除 XSS 攻擊模式
        for (Pattern pattern : XSS_PATTERNS) {
            sanitized = pattern.matcher(sanitized).replaceAll("");
        }

        // 轉義特殊字元
        for (String[] escape : XSS_ESCAPE_CHARS) {
            sanitized = sanitized.replace(escape[0], escape[1]);
        }

        return sanitized;
    }

    // 檢查是否包含 SQL 注入關鍵字
    public static boolean containsSqlInjection(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        String upperInput = input.toUpperCase();

        // 檢查 SQL 關鍵字
        for (String keyword : SQL_INJECTION_KEYWORDS) {
            if (upperInput.contains(keyword)) {
                return true;
            }
        }

        // 檢查特殊字元組合
        if (upperInput.contains("'") && (
                upperInput.contains("OR") ||
                        upperInput.contains("AND") ||
                        upperInput.contains("="))) {
            return true;
        }

        // 檢查註解符號
        if (input.contains("--") || input.contains("/*") || input.contains("*/")) {
            return true;
        }

        return false;
    }

    // 檢查是否包含 XSS 攻擊
    public static boolean containsXSS(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // 檢查 XSS 模式
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }

        return false;
    }

    // 驗證員工編號格式
    public static boolean isValidEmpId(String empId) {
        if (empId == null || empId.isEmpty()) {
            return false;
        }
        return EMP_ID_PATTERN.matcher(empId).matches();
    }

    // 清理 SQL 注入字串
    public static String sanitizeSqlInjection(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String sanitized = input;

        // 移除 SQL 關鍵字
        for (String keyword : SQL_INJECTION_KEYWORDS) {
            sanitized = sanitized.replaceAll("(?i)" + keyword, "");
        }

        // 移除特殊字元
        sanitized = sanitized.replace("'", "");
        sanitized = sanitized.replace("\"", "");
        sanitized = sanitized.replace("--", "");
        sanitized = sanitized.replace("/*", "");
        sanitized = sanitized.replace("*/", "");
        sanitized = sanitized.replace(";", "");

        return sanitized;
    }

    // 全面清理輸入字串（XSS + SQL 注入）
    public static String sanitizeInput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 先清理 SQL 注入
        String sanitized = sanitizeSqlInjection(input);

        // 再清理 XSS
        sanitized = sanitizeXSS(sanitized);

        return sanitized;
    }

    // 驗證輸入安全性
    public static boolean isInputSafe(String input) {
        if (input == null) {
            return true;
        }

        // 檢查 SQL 注入
        if (containsSqlInjection(input)) {
            return false;
        }

        // 檢查 XSS
        if (containsXSS(input)) {
            return false;
        }

        return true;
    }

    // HTML 編碼
    public static String htmlEncode(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder encoded = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '<':
                    encoded.append("&lt;");
                    break;
                case '>':
                    encoded.append("&gt;");
                    break;
                case '"':
                    encoded.append("&quot;");
                    break;
                case '\'':
                    encoded.append("&#x27;");
                    break;
                case '&':
                    encoded.append("&amp;");
                    break;
                default:
                    encoded.append(c);
            }
        }
        return encoded.toString();
    }

    // URL 編碼
    public static String urlEncode(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            return java.net.URLEncoder.encode(input, "UTF-8");
        } catch (Exception e) {
            return input;
        }
    }

    // 私有建構子，防止實例化
    private SecurityUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}