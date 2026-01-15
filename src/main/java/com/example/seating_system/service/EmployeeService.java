package com.example.seating_system.service;

import com.example.seating_system.dto.response.EmployeeResponse;

import java.util.List;
import java.util.Map;

// 員工服務介面
public interface EmployeeService {

    // 查詢所有員工
    List<EmployeeResponse> getAllEmployees();

    // 查詢單一員工
    EmployeeResponse getEmployeeById(String empId);

    // 查詢未分配座位的員工
    List<EmployeeResponse> getUnassignedEmployees();

    // 查詢已分配座位的員工
    List<EmployeeResponse> getAssignedEmployees();

    // 查詢指定樓層的員工
    List<EmployeeResponse> getEmployeesByFloor(Integer floorNo);

    // 搜尋員工（依姓名）
    List<EmployeeResponse> searchEmployees(String name);

    // 查詢員工統計
    Map<String, Object> getEmployeeStatistics();

    // 檢查員工是否存在
    boolean employeeExists(String empId);

    // 檢查員工是否已有座位
    boolean employeeHasSeat(String empId);
}
