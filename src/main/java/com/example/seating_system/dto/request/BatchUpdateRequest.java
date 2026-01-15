package com.example.seating_system.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// 批次更新請求 DTO
public class BatchUpdateRequest {

    @NotNull(message = "分配清單不能為空")
    @NotEmpty(message = "分配清單不能為空")
    @Valid
    private List<AssignSeatRequest> assignments;

    // 建構子
    public BatchUpdateRequest() {
    }

    public BatchUpdateRequest(List<AssignSeatRequest> assignments) {
        this.assignments = assignments;
    }

    // Getter 和 Setter
    public List<AssignSeatRequest> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignSeatRequest> assignments) {
        this.assignments = assignments;
    }

    @Override
    public String toString() {
        return "BatchUpdateRequest{" +
                "assignments=" + assignments +
                '}';
    }
}