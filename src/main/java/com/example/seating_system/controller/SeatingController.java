package com.example.seating_system.controller;

import com.example.seating_system.dto.request.AssignSeatRequest;
import com.example.seating_system.dto.request.BatchUpdateRequest;
import com.example.seating_system.dto.response.ApiResponse;
import com.example.seating_system.dto.response.SeatInfoResponse;
import com.example.seating_system.service.SeatingService;
import com.example.seating_system.util.Constants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


// 座位管理 Controller
@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatingController {

    @Autowired
    private SeatingService seatingService;

    // 查詢所有座位
    @GetMapping
    public ResponseEntity<ApiResponse<List<SeatInfoResponse>>> getAllSeats() {
        List<SeatInfoResponse> seats = seatingService.getAllSeats();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", seats));
    }

    // 查詢單一座位
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SeatInfoResponse>> getSeatById(@PathVariable Integer id) {
        SeatInfoResponse seat = seatingService.getSeatById(id);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", seat));
    }

    // 查詢指定樓層的座位
    @GetMapping("/floor/{floorNo}")
    public ResponseEntity<ApiResponse<List<SeatInfoResponse>>> getSeatsByFloor(@PathVariable Integer floorNo) {
        List<SeatInfoResponse> seats = seatingService.getSeatsByFloor(floorNo);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", seats));
    }

    // 查詢所有空位
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<SeatInfoResponse>>> getAvailableSeats() {
        List<SeatInfoResponse> seats = seatingService.getAvailableSeats();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", seats));
    }

    // 查詢已佔用的座位
    @GetMapping("/occupied")
    public ResponseEntity<ApiResponse<List<SeatInfoResponse>>> getOccupiedSeats() {
        List<SeatInfoResponse> seats = seatingService.getOccupiedSeats();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", seats));
    }

    // 查詢所有樓層編號
    @GetMapping("/floors")
    public ResponseEntity<ApiResponse<List<Integer>>> getAllFloors() {
        List<Integer> floors = seatingService.getAllFloors();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", floors));
    }

    // 分配座位
    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<String>> assignSeat(@Valid @RequestBody AssignSeatRequest request) {
        String empId = seatingService.assignSeatUsingSP(request);
        return ResponseEntity.ok(ApiResponse.success("座位分配成功", empId));
    }

    // 清除座位
    @DeleteMapping("/clear/{empId}")
    public ResponseEntity<ApiResponse<Void>> clearSeat(@PathVariable String empId) {
        seatingService.clearSeatUsingSP(empId);
        return ResponseEntity.ok(ApiResponse.success("座位已清除", null));
    }

    // 批次更新座位
    @PostMapping("/batch-update")
    public ResponseEntity<ApiResponse<Map<String, Object>>> batchUpdate(@Valid @RequestBody BatchUpdateRequest request) {
        Map<String, Object> result = seatingService.batchUpdate(request);

        int successCount = (int) result.get("successCount");
        int failCount = (int) result.get("failCount");

        if (failCount == 0) {
            return ResponseEntity.ok(ApiResponse.success("全部更新成功", result));
        } else if (successCount > 0) {
            return ResponseEntity.ok(ApiResponse.partialSuccess(
                    String.format("部分更新成功（成功：%d，失敗：%d）", successCount, failCount),
                    result
            ));
        } else {
            return ResponseEntity.ok(ApiResponse.error("全部更新失敗", Constants.ErrorCode.BUSINESS_ERROR, result));
        }
    }

    // 查詢座位統計
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSeatStatistics() {
        Map<String, Object> statistics = seatingService.getSeatStatistics();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", statistics));
    }

    // 檢查座位是否可用
    @GetMapping("/{id}/available")
    public ResponseEntity<ApiResponse<Boolean>> isSeatAvailable(@PathVariable Integer id) {
        boolean available = seatingService.isSeatAvailable(id);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", available));
    }

    // 檢查座位是否存在
    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> seatExists(@PathVariable Integer id) {
        boolean exists = seatingService.seatExists(id);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", exists));
    }
}