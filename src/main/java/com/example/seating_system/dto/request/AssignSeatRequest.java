package com.example.seating_system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

// 座位分配請求 DTO
public class AssignSeatRequest {

    @NotNull(message = "員工編號不能為空")
    @Pattern(regexp = "^\\d{5}$", message = "員工編號必須為 5 位數字")
    private String empId;

    @NotNull(message = "座位序號不能為空")
    @Positive(message = "座位序號必須大於 0")
    private Integer floorSeatSeq;

    // 建構子
    public AssignSeatRequest() {
    }

    public AssignSeatRequest(String empId, Integer floorSeatSeq) {
        this.empId = empId;
        this.floorSeatSeq = floorSeatSeq;
    }

    // Getter 和 Setter
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Integer getFloorSeatSeq() {
        return floorSeatSeq;
    }

    public void setFloorSeatSeq(Integer floorSeatSeq) {
        this.floorSeatSeq = floorSeatSeq;
    }

    @Override
    public String toString() {
        return "AssignSeatRequest{" +
                "empId='" + empId + '\'' +
                ", floorSeatSeq=" + floorSeatSeq +
                '}';
    }
}