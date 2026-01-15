package com.example.seating_system.repository;

import com.example.seating_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 員工資料存取層
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    // 根據座位序號查詢員工（返回單一結果，可能為 null）
    Employee findFirstByFloorSeatSeq(Integer floorSeatSeq);

    // 查詢未分配座位的員工
    List<Employee> findByFloorSeatSeqIsNull();

    // 查詢已分配座位的員工
    List<Employee> findByFloorSeatSeqIsNotNull();

    // 根據姓名模糊搜尋員工
    List<Employee> findByNameContaining(String name);

    // 查詢指定樓層的員工
    @Query("SELECT e FROM Employee e JOIN SeatingChart s ON e.floorSeatSeq = s.floorSeatSeq WHERE s.floorNo = :floorNo")
    List<Employee> findByFloorNo(@Param("floorNo") Integer floorNo);

    // 呼叫 SP_GET_ALL_EMPLOYEES（查詢所有員工）
    @Query(value = "CALL SP_GET_ALL_EMPLOYEES()", nativeQuery = true)
    List<Object[]> callGetAllEmployees();
}