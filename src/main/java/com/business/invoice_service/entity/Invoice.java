package com.business.invoice_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "bill_date")
    private LocalDateTime billDate;

    @Column(name = "total_money")
    private double totalMoney;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

//    @Column(name = "booking_id")
    @Column(name = "booking_id")
    private Integer bookingId;  // Chỉ lưu trữ ID của Booking (khóa ngoại)

    @Column(name = "table_id")
    private Integer tableId;

    public Invoice() {

    }
    public Invoice(Integer id, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime billDate, double totalMoney, String status, Integer bookingId, Integer tableId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.billDate = billDate;
        this.totalMoney = totalMoney;
        this.status = status;
        this.bookingId = bookingId;
        this.tableId = tableId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
}
