package com.example.seating_system.service;

import com.example.seating_system.dto.request.AssignSeatRequest;
import com.example.seating_system.dto.request.BatchUpdateRequest;
import com.example.seating_system.dto.response.SeatInfoResponse;
import com.example.seating_system.entity.Employee;
import com.example.seating_system.entity.SeatingChart;
import com.example.seating_system.exception.BusinessException;
import com.example.seating_system.exception.ResourceNotFoundException;
import com.example.seating_system.exception.SeatOccupiedException;
import com.example.seating_system.repository.EmployeeRepository;
import com.example.seating_system.repository.SeatingRepository;
import com.example.seating_system.util.Constants;
import com.example.seating_system.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

// 座位服務實作
@Service
@Transactional
public class SeatingServiceImpl implements SeatingService {

    @Autowired
    private SeatingRepository seatingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<SeatInfoResponse> getAllSeats() {
        List<SeatingChart> seats = seatingRepository.findAll();
        return convertToSeatInfoList(seats);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatInfoResponse getSeatById(Integer floorSeatSeq) {
        SeatingChart seat = seatingRepository.findById(floorSeatSeq)
                .orElseThrow(() -> new ResourceNotFoundException("座位不存在"));
        return convertToSeatInfo(seat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatInfoResponse> getSeatsByFloor(Integer floorNo) {
        List<SeatingChart> seats = seatingRepository.findByFloorNo(floorNo);
        return convertToSeatInfoList(seats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatInfoResponse> getAvailableSeats() {
        List<SeatingChart> allSeats = seatingRepository.findAll();
        List<String> occupiedSeatIds = employeeRepository.findAll().stream()
                .map(Employee::getFloorSeatSeq)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.toList());

        return allSeats.stream()
                .filter(seat -> !occupiedSeatIds.contains(String.valueOf(seat.getFloorSeatSeq())))
                .map(this::convertToSeatInfo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatInfoResponse> getOccupiedSeats() {
        List<Employee> employees = employeeRepository.findAll();
        Set<Integer> occupiedSeatIds = employees.stream()
                .map(Employee::getFloorSeatSeq)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<SeatingChart> allSeats = seatingRepository.findAll();
        return allSeats.stream()
                .filter(seat -> occupiedSeatIds.contains(seat.getFloorSeatSeq()))
                .map(seat -> {
                    Employee employee = employees.stream()
                            .filter(e -> seat.getFloorSeatSeq().equals(e.getFloorSeatSeq()))
                            .findFirst()
                            .orElse(null);
                    return convertToSeatInfo(seat, employee);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getAllFloors() {
        return seatingRepository.findAllFloors();
    }

    @Override
    public String assignSeatUsingSP(AssignSeatRequest request) {
        ValidationUtil.validateAssignSeatRequest(request);

        try {
            // 使用 EntityManager 直接執行 Stored Procedure
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ASSIGN_SEAT");

            // 註冊參數
            query.registerStoredProcedureParameter("p_emp_id", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_floor_seat_seq", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_result", Integer.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("p_message", String.class, ParameterMode.OUT);

            // 設定輸入參數
            query.setParameter("p_emp_id", request.getEmpId());
            query.setParameter("p_floor_seat_seq", request.getFloorSeatSeq());

            // 執行
            query.execute();

            // 取得輸出參數
            Integer result = (Integer) query.getOutputParameterValue("p_result");
            String message = (String) query.getOutputParameterValue("p_message");

            if (result != null && result == 1) {
                return request.getEmpId();
            } else {
                throw new BusinessException(message != null ? message : "座位分配失敗");
            }
        } catch (Exception e) {
            throw new BusinessException("座位分配失敗: " + e.getMessage());
        }
    }

    @Override
    public void clearSeatUsingSP(String empId) {
        ValidationUtil.validateEmpId(empId);

        try {
            // 使用 EntityManager 直接執行 Stored Procedure
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_CLEAR_SEAT");

            // 註冊參數
            query.registerStoredProcedureParameter("p_emp_id", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_result", Integer.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("p_message", String.class, ParameterMode.OUT);

            // 設定輸入參數
            query.setParameter("p_emp_id", empId);

            // 執行
            query.execute();

            // 取得輸出參數
            Integer result = (Integer) query.getOutputParameterValue("p_result");
            String message = (String) query.getOutputParameterValue("p_message");

            if (result == null || result != 1) {
                throw new BusinessException(message != null ? message : "座位清除失敗");
            }
        } catch (Exception e) {
            throw new BusinessException("座位清除失敗: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> batchUpdate(BatchUpdateRequest request) {
        ValidationUtil.validateBatchUpdateRequest(request);

        Map<String, Object> result = new HashMap<>();
        List<String> successList = new ArrayList<>();
        List<Map<String, String>> failList = new ArrayList<>();

        for (AssignSeatRequest assignment : request.getAssignments()) {
            try {
                String empId = assignSeatUsingSP(assignment);
                successList.add(empId);
            } catch (Exception e) {
                Map<String, String> failure = new HashMap<>();
                failure.put("empId", assignment.getEmpId());
                failure.put("reason", e.getMessage());
                failList.add(failure);
            }
        }

        result.put("successCount", successList.size());
        result.put("failCount", failList.size());
        result.put("successList", successList);
        result.put("failList", failList);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSeatStatistics() {
        List<SeatingChart> allSeats = seatingRepository.findAll();
        List<Employee> employees = employeeRepository.findAll();

        Set<Integer> occupiedSeatIds = employees.stream()
                .map(Employee::getFloorSeatSeq)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int totalSeats = allSeats.size();
        int occupiedSeats = occupiedSeatIds.size();
        int availableSeats = totalSeats - occupiedSeats;

        Map<Integer, List<SeatingChart>> seatsByFloor = allSeats.stream()
                .collect(Collectors.groupingBy(SeatingChart::getFloorNo));

        List<Map<String, Object>> floorStatistics = new ArrayList<>();
        for (Map.Entry<Integer, List<SeatingChart>> entry : seatsByFloor.entrySet()) {
            Integer floorNo = entry.getKey();
            List<SeatingChart> seats = entry.getValue();

            long occupied = seats.stream()
                    .filter(seat -> occupiedSeatIds.contains(seat.getFloorSeatSeq()))
                    .count();

            Map<String, Object> floorStat = new HashMap<>();
            floorStat.put("floorNo", floorNo);
            floorStat.put("totalSeats", seats.size());
            floorStat.put("occupiedSeats", occupied);
            floorStat.put("availableSeats", seats.size() - occupied);
            floorStatistics.add(floorStat);
        }

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSeats", totalSeats);
        statistics.put("occupiedSeats", occupiedSeats);
        statistics.put("availableSeats", availableSeats);
        statistics.put("floorStatistics", floorStatistics);

        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSeatAvailable(Integer floorSeatSeq) {
        Employee employee = employeeRepository.findFirstByFloorSeatSeq(floorSeatSeq);
        return employee == null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean seatExists(Integer floorSeatSeq) {
        return seatingRepository.existsById(floorSeatSeq);
    }

    // 私有輔助方法
    private SeatInfoResponse convertToSeatInfo(SeatingChart seat) {
        Employee employee = employeeRepository.findFirstByFloorSeatSeq(seat.getFloorSeatSeq());
        return convertToSeatInfo(seat, employee);
    }

    private SeatInfoResponse convertToSeatInfo(SeatingChart seat, Employee employee) {
        SeatInfoResponse response = new SeatInfoResponse();
        response.setFloorSeatSeq(seat.getFloorSeatSeq());
        response.setFloorNo(seat.getFloorNo());
        response.setSeatNo(seat.getSeatNo());

        if (employee != null) {
            response.setEmpId(employee.getEmpId());
            response.setEmpName(employee.getName());
            response.setEmail(employee.getEmail());
            response.setStatus(Constants.SeatStatus.OCCUPIED);
        } else {
            response.setStatus(Constants.SeatStatus.AVAILABLE);
        }

        return response;
    }

    private List<SeatInfoResponse> convertToSeatInfoList(List<SeatingChart> seats) {
        return seats.stream()
                .map(this::convertToSeatInfo)
                .collect(Collectors.toList());
    }
}