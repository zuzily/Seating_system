package com.example.seating_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seatingchart")
public class SeatingChart {

    // 座位序號（主鍵，自動遞增）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FLOOR_SEAT_SEQ")
    private Integer floorSeatSeq;

    // 樓層編號
    @Column(name = "FLOOR_NO", nullable = false)
    private Integer floorNo;

    // 座位編號
    @Column(name = "SEAT_NO", length = 10, nullable = false)
    private String seatNo;

    // 建立時間
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    // 更新時間
    @Column(name = "UPDATED_DATE", nullable = false)
    private LocalDateTime updatedDate;

    // 預設建構子
    public SeatingChart() {
    }

    // 完整建構子
    public SeatingChart(Integer floorNo, String seatNo) {
        this.floorNo = floorNo;
        this.seatNo = seatNo;
    }

    // JPA 生命週期回調
    // 插入前自動設定建立時間和更新時間
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // 更新前自動設定更新時間
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    // toString、equals、hashCode
    @Override
    public String toString() {
        return "SeatingChart{" +
                "floorSeatSeq=" + floorSeatSeq +
                ", floorNo=" + floorNo +
                ", seatNo='" + seatNo + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatingChart that = (SeatingChart) o;
        return floorSeatSeq != null && floorSeatSeq.equals(that.floorSeatSeq);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
