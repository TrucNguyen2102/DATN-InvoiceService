package com.business.invoice_service.dto;

import java.time.LocalDate;
import java.util.Date;

public class RevenueChartData {
    private Date date;

    private Double totalMoney;



    public RevenueChartData(Date date, Double totalMoney) {
        this.date = date;
        this.totalMoney = totalMoney;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
