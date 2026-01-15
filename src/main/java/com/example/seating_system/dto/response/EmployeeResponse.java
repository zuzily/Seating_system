package com.example.seating_system.dto.response;

public class EmployeeResponse {

    // 員工編號
    private String empId;

    // 員工姓名
    private String name;

    // 員工電子郵件
    private String email;

    // 座位序號（可能為空）
    private Integer floorSeatSeq;

    // 樓層編號（可能為空）
    private Integer floorNo;

    // 座位編號（可能為空）
    private String seatNo;

    // 是否有座位
    private boolean hasSeat;

    // 建構子
    public EmployeeResponse() {
    }

    public EmployeeResponse(String empId, String name, String email) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.hasSeat = false;
    }

    public EmployeeResponse(String empId, String name, String email,
                            Integer floorSeatSeq, Integer floorNo, String seatNo) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.floorSeatSeq = floorSeatSeq;
        this.floorNo = floorNo;
        this.seatNo = seatNo;
        this.hasSeat = floorSeatSeq != null;
    }

    // Getter 和 Setter
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFloorSeatSeq() {
        return floorSeatSeq;
    }

    public void setFloorSeatSeq(Integer floorSeatSeq) {
        this.floorSeatSeq = floorSeatSeq;
        this.hasSeat = floorSeatSeq != null;
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

    public boolean isHasSeat() {
        return hasSeat;
    }

    public void setHasSeat(boolean hasSeat) {
        this.hasSeat = hasSeat;
    }

    public String getSeatDescription() {
        if (hasSeat) {
            return floorNo + "樓" + seatNo;
        }
        return "未分配";
    }

    @Override
    public String toString() {
        return "EmployeeResponse{" +
                "empId='" + empId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", floorSeatSeq=" + floorSeatSeq +
                ", floorNo=" + floorNo +
                ", seatNo='" + seatNo + '\'' +
                ", hasSeat=" + hasSeat +
                '}';
    }
}
