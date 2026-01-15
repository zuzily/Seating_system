package com.example.seating_system.service;

import com.example.seating_system.dto.response.EmployeeResponse;
import com.example.seating_system.entity.Employee;
import com.example.seating_system.entity.SeatingChart;
import com.example.seating_system.exception.ResourceNotFoundException;
import com.example.seating_system.repository.EmployeeRepository;
import com.example.seating_system.repository.SeatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 員工服務實作
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SeatingRepository seatingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(String empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("員工不存在"));
        return convertToResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getUnassignedEmployees() {
        List<Employee> employees = employeeRepository.findByFloorSeatSeqIsNull();
        return employees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAssignedEmployees() {
        List<Employee> employees = employeeRepository.findByFloorSeatSeqIsNotNull();
        return employees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByFloor(Integer floorNo) {
        List<Employee> employees = employeeRepository.findByFloorNo(floorNo);
        return employees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> searchEmployees(String name) {
        List<Employee> employees = employeeRepository.findByNameContaining(name);
        return employees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEmployeeStatistics() {
        List<Employee> allEmployees = employeeRepository.findAll();

        long totalEmployees = allEmployees.size();
        long assignedEmployees = allEmployees.stream()
                .filter(e -> e.getFloorSeatSeq() != null)
                .count();
        long unassignedEmployees = totalEmployees - assignedEmployees;

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalEmployees", totalEmployees);
        statistics.put("assignedEmployees", assignedEmployees);
        statistics.put("unassignedEmployees", unassignedEmployees);

        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean employeeExists(String empId) {
        return employeeRepository.existsById(empId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean employeeHasSeat(String empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("員工不存在"));
        return employee.getFloorSeatSeq() != null;
    }

    // 私有輔助方法
    private EmployeeResponse convertToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmpId(employee.getEmpId());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        response.setFloorSeatSeq(employee.getFloorSeatSeq());

        // 如果員工有座位，查詢座位資訊
        if (employee.getFloorSeatSeq() != null) {
            seatingRepository.findById(employee.getFloorSeatSeq())
                    .ifPresent(seat -> {
                        response.setFloorNo(seat.getFloorNo());
                        response.setSeatNo(seat.getSeatNo());
                        response.setHasSeat(true);
                    });
        } else {
            response.setHasSeat(false);
        }

        return response;
    }
}
