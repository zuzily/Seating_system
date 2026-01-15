package com.example.seating_system.config;

import com.example.seating_system.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(1);
        return registration;
    }

    // XSS 過濾器
    public static class XssFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            // 初始化
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            // 包裝請求，過濾 XSS
            XssHttpServletRequestWrapper wrappedRequest =
                    new XssHttpServletRequestWrapper((HttpServletRequest) request);

            chain.doFilter(wrappedRequest, response);
        }

        @Override
        public void destroy() {
            // 清理
        }
    }

    // XSS 請求包裝器
    public static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        // 過濾請求參數
        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);

            if (values == null) {
                return null;
            }

            int count = values.length;
            String[] encodedValues = new String[count];

            for (int i = 0; i < count; i++) {
                encodedValues[i] = cleanXSS(values[i]);
            }

            return encodedValues;
        }

        // 過濾單一參數
        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            return cleanXSS(value);
        }

        // 過濾 Header
        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return cleanXSS(value);
        }

        // 清理 XSS 攻擊字元
        private String cleanXSS(String value) {
            if (value == null) {
                return null;
            }

            return SecurityUtil.sanitizeXSS(value);
        }
    }
}
