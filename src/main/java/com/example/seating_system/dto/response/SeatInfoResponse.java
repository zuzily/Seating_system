package com.example.seating_system.dto.response;

public class SeatInfoResponse {
    // 座位序號
    private Integer floorSeatSeq;

    // 樓層編號
    private Integer floorNo;

    // 座位編號
    private String seatNo;

    // 員工編號（可能為空）
    private String empId;

    // 員工姓名（可能為空）
    private String empName;

    // 員工電子郵件（可能為空）
    private String email;

    // 座位狀態（occupied / available / selected）
    private String status;

    // 建構子
    public SeatInfoResponse() {
    }

    public SeatInfoResponse(Integer floorSeatSeq, Integer floorNo, String seatNo) {
        this.floorSeatSeq = floorSeatSeq;
        this.floorNo = floorNo;
        this.seatNo = seatNo;
        this.status = "available";
    }

    public SeatInfoResponse(Integer floorSeatSeq, Integer floorNo, String seatNo,
                            String empId, String empName, String email) {
        this.floorSeatSeq = floorSeatSeq;
        this.floorNo = floorNo;
        this.seatNo = seatNo;
        this.empId = empId;
        this.empName = empName;
        this.email = email;
        this.status = empId != null ? "occupied" : "available";
    }

    // Getter 和 Setter
    public Integer getFloorSeatSeq() {
        return floorSeatSeq;
    }

    public void setFloorSeatSeq(Integer floorSeatSeq) {
        this.floorSeatSeq = floorSeatSeq;
    }

    public Integer getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(Integer floorNo) {
        this.floorNo = floorNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
        // 自動更新狀態
        this.status = empId != null ? "occupied" : "available";
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 判斷座位是否已被佔用
    public boolean isOccupied() {
        return empId != null;
    }

    // 判斷座位是否為空位
    public boolean isAvailable() {
        return empId == null;
    }

    // toString
    @Override
    public String toString() {
        return "SeatInfoResponse{" +
                "floorSeatSeq=" + floorSeatSeq +
                ", floorNo=" + floorNo +
                ", seatNo='" + seatNo + '\'' +
                ", empId='" + empId + '\'' +
                ", empName='" + empName + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
