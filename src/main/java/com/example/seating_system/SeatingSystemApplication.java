package com.example.seating_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SeatingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeatingSystemApplication.class, args);

        // 啟動成功後顯示訊息
        System.out.println("\n========================================");
        System.out.println("Employee Seating System Started!");
        System.out.println("========================================");
        System.out.println("Application: http://localhost:8080");
        System.out.println("========================================\n");
    }
}









