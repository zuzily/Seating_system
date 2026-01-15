package com.example.seating_system.repository;

import com.example.seating_system.entity.SeatingChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

// 座位資料存取層
@Repository
public interface SeatingRepository extends JpaRepository<SeatingChart, Integer> {

    // 查詢指定樓層的所有座位
    List<SeatingChart> findByFloorNo(Integer floorNo);

    // 查詢所有樓層編號
    @Query("SELECT DISTINCT s.floorNo FROM SeatingChart s ORDER BY s.floorNo")
    List<Integer> findAllFloors();

//    呼叫 SP_GET_ALL_SEATS（查詢所有座位）
//    使用 @Query 代替 @Procedure，避免 ResultSet 問題
    @Query(value = "CALL SP_GET_ALL_SEATS()", nativeQuery = true)
    List<Map<String, Object>> callGetAllSeats();

//    呼叫 SP_ASSIGN_SEAT（分配座位）
//    改用簡化的 CALL 語法
    @Query(value = "CALL SP_ASSIGN_SEAT(:empId, :floorSeatSeq, @result, @message)", nativeQuery = true)
    void callAssignSeatSimple(@Param("empId") String empId, @Param("floorSeatSeq") Integer floorSeatSeq);

    // 取得 SP_ASSIGN_SEAT 的執行結果
    @Query(value = "SELECT @result as result, @message as message", nativeQuery = true)
    Map<String, Object> getAssignSeatResult();

    // 呼叫 SP_CLEAR_SEAT（清除座位）
    @Query(value = "CALL SP_CLEAR_SEAT(:empId, @result, @message)", nativeQuery = true)
    void callClearSeatSimple(@Param("empId") String empId);

    // 取得 SP_CLEAR_SEAT 的執行結果
    @Query(value = "SELECT @result as result, @message as message", nativeQuery = true)
    Map<String, Object> getClearSeatResult();
}