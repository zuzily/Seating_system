package com.example.seating_system.controller;

import com.example.seating_system.dto.response.ApiResponse;
import com.example.seating_system.dto.response.EmployeeResponse;
import com.example.seating_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


//員工管理 Controller
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 查詢所有員工
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", employees));
    }

    // 查詢單一員工
    @GetMapping("/{empId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable String empId) {
        EmployeeResponse employee = employeeService.getEmployeeById(empId);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", employee));
    }

    // 查詢未分配座位的員工
    @GetMapping("/unassigned")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getUnassignedEmployees() {
        List<EmployeeResponse> employees = employeeService.getUnassignedEmployees();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", employees));
    }

    // 查詢已分配座位的員工
    @GetMapping("/assigned")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAssignedEmployees() {
        List<EmployeeResponse> employees = employeeService.getAssignedEmployees();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", employees));
    }

    // 查詢指定樓層的員工
    @GetMapping("/floor/{floorNo}")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getEmployeesByFloor(@PathVariable Integer floorNo) {
        List<EmployeeResponse> employees = employeeService.getEmployeesByFloor(floorNo);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", employees));
    }

    // 搜尋員工（依姓名）
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> searchEmployees(@RequestParam String name) {
        List<EmployeeResponse> employees = employeeService.searchEmployees(name);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", employees));
    }

    // 查詢員工統計
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEmployeeStatistics() {
        Map<String, Object> statistics = employeeService.getEmployeeStatistics();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", statistics));
    }


    //檢查員工是否存在
    @GetMapping("/{empId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> employeeExists(@PathVariable String empId) {
        boolean exists = employeeService.employeeExists(empId);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", exists));
    }

    // 檢查員工是否已有座位
    @GetMapping("/{empId}/has-seat")
    public ResponseEntity<ApiResponse<Boolean>> employeeHasSeat(@PathVariable String empId) {
        boolean hasSeat = employeeService.employeeHasSeat(empId);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", hasSeat));
    }
}
