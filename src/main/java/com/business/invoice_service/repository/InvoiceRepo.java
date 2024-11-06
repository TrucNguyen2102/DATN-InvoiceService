package com.business.invoice_service.repository;

import com.business.invoice_service.dto.RevenueChartData;
import com.business.invoice_service.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {
    Optional<Invoice> findByBookingId(Integer bookingId);

    @Query("SELECT COALESCE(SUM(i.totalMoney), 0) FROM Invoice i WHERE DATE(i.billDate) = :date")
    Double getDailyRevenue(@Param("date") LocalDate date);

    // phương thức lấy dữ liệu cho biểu đồ doanh thu theo ngày
    @Query("SELECT new com.business.invoice_service.dto.RevenueChartData(DATE(i.billDate), COALESCE(SUM(i.totalMoney), 0)) " +
            "FROM Invoice i WHERE i.billDate >= :startDate AND i.billDate < :endDate GROUP BY DATE(i.billDate)")
    List<RevenueChartData> getRevenueChartData(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT SUM(i.totalMoney) FROM Invoice i WHERE MONTH(i.billDate) = MONTH(:date) AND YEAR(i.billDate) = YEAR(:date)")
    double getMonthlyRevenue(@Param("date") LocalDate date);

    @Query("SELECT SUM(i.totalMoney) FROM Invoice i WHERE YEAR(i.billDate) = YEAR(:date)")
    double getYearlyRevenue(@Param("date") LocalDate date);

}
