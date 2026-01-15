package com.example.seating_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
public class Employee {

    // 員工編號（主鍵，固定5碼）
    @Id
    @Column(name = "EMP_ID", length = 5, nullable = false)
    private String empId;

    // 員工姓名
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    // 員工電子郵件（唯一）
    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;

    // 座位資訊（外鍵，可為空）
    @Column(name = "FLOOR_SEAT_SEQ")
    private Integer floorSeatSeq;

    // 建立時間
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    // 更新時間
    @Column(name = "UPDATED_DATE", nullable = false)
    private LocalDateTime updatedDate;

    // 預設建構子
    public Employee() {
    }

    // 完整建構子
    public Employee(String empId, String name, String email, Integer floorSeatSeq) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.floorSeatSeq = floorSeatSeq;
    }

    // JPA 生命週期回調
    // 插入前自動設定建立時間和更新時間
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // 更新前自動設定更新時間
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    // Getter 和 Setter
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFloorSeatSeq() {
        return floorSeatSeq;
    }

    public void setFloorSeatSeq(Integer floorSeatSeq) {
        this.floorSeatSeq = floorSeatSeq;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    // toString、equals、hashCode
    @Override
    public String toString() {
        return "Employee{" +
                "empId='" + empId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", floorSeatSeq=" + floorSeatSeq +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId != null && empId.equals(employee.empId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
