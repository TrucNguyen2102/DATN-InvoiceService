package com.business.invoice_service.repository;

import com.business.invoice_service.dto.PaymentDTO;
import com.business.invoice_service.dto.RevenueChartData;
import com.business.invoice_service.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    // Tìm hóa đơn theo bookingId và tableId
//    Invoice findByBookingIdAndTableId(Integer bookingId, Integer tableId);

    // Truy vấn tất cả hóa đơn theo bookingId
    List<Invoice> findAllByBookingId(Integer bookingId);

//    @Query("SELECT i FROM Invoice i WHERE i.bookingId = :bookingId")
//    List<Invoice> findAllByBookingId(@Param("bookingId") Integer bookingId);

    @Query("SELECT i FROM Invoice i WHERE i.bookingId = :bookingId AND DATE(i.startTime) = :date")
    List<Invoice> findAllByBookingIdAndDate(@Param("bookingId") Integer bookingId, @Param("date") LocalDate date);


    //tìm hóa đơn theo bàn
    Optional<Invoice> findByTableId(Integer tableId);
    Optional<Invoice> findTopByTableIdAndStatusOrderByBillDateDesc(Integer tableId, String status);
    List<Invoice> findInvoiceByBookingId(Integer bookingId);

    // Tìm hóa đơn theo tableId và status
    Optional<Invoice> findByTableIdAndStatus(Integer tableId, String status);

    // Tìm hóa đơn theo ngày
//    List<Invoice> findByBillDate(LocalDate billDate);
    // Tìm tất cả các hóa đơn có billDate trong khoảng thời gian của ngày cụ thể
//    List<Invoice> findByBillDateBetween(LocalDateTime start, LocalDateTime end);

//    @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, i.startTime, i.endTime)) FROM Invoice i WHERE DATE(i.billDate) = :date")
//    Integer calculateTotalPlayTimeInMinutes(@Param("date") LocalDate date);

    @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, i.startTime, i.endTime)) " +
            "FROM Invoice i WHERE i.startTime BETWEEN :startDate AND :endDate")
    Integer calculateTotalPlayTimeInMinutes(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);



    // Phương thức kiểm tra xem có tồn tại Invoice nào có tableId không
    boolean existsByTableId(Integer tableId);

    @Query("SELECT i.methodId, SUM(i.totalMoney) FROM Invoice i WHERE i.status = 'PAID' GROUP BY i.methodId")
    List<Object[]> getTotalInvoicesByPaymentMethod();


    @Query("SELECT pm.name AS paymentMethod, COUNT(*) AS invoiceCount " +
            "FROM Invoice i JOIN PaymentMethod pm ON i.methodId = pm.id " +
            "GROUP BY pm.name")
    List<Object[]> countInvoicesByPaymentMethod();

    @Query("SELECT pm.name AS paymentMethod, SUM(i.totalMoney) AS totalMoney " +
            "FROM Invoice i JOIN PaymentMethod pm ON i.methodId = pm.id " +
            "GROUP BY pm.name")
    List<Object[]> findTotalAmountByPaymentMethod();


    @Query("SELECT SUM(i.totalMoney) FROM Invoice i WHERE i.billDate BETWEEN :startDate AND :endDate")
    double calculateRevenueByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE(i.billDate), SUM(i.totalMoney) FROM Invoice i WHERE i.billDate BETWEEN :startDate AND :endDate GROUP BY DATE(i.billDate)")
    List<Object[]> calculateChartData(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
