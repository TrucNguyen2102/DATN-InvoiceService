package com.business.invoice_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UpdateBillDateRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billDate;

    private double totalMoney;

    private String status;

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
}
