package com.example.seating_system.service;

import com.example.seating_system.dto.request.AssignSeatRequest;
import com.example.seating_system.dto.request.BatchUpdateRequest;
import com.example.seating_system.dto.response.SeatInfoResponse;

import java.util.List;
import java.util.Map;

// 座位服務介面
public interface SeatingService {

    // 查詢所有座位
    List<SeatInfoResponse> getAllSeats();

    // 查詢單一座位
    SeatInfoResponse getSeatById(Integer floorSeatSeq);

    // 查詢指定樓層的座位
    List<SeatInfoResponse> getSeatsByFloor(Integer floorNo);

    // 查詢所有空位
    List<SeatInfoResponse> getAvailableSeats();

    // 查詢已佔用的座位
    List<SeatInfoResponse> getOccupiedSeats();

    // 查詢所有樓層編號
    List<Integer> getAllFloors();

    // 分配座位（使用 Stored Procedure）
    String assignSeatUsingSP(AssignSeatRequest request);

    // 清除座位（使用 Stored Procedure）
    void clearSeatUsingSP(String empId);

    // 批次更新座位
    Map<String, Object> batchUpdate(BatchUpdateRequest request);

    // 查詢座位統計
    Map<String, Object> getSeatStatistics();

    // 檢查座位是否可用
    boolean isSeatAvailable(Integer floorSeatSeq);

    // 檢查座位是否存在
    boolean seatExists(Integer floorSeatSeq);
}