package com.business.invoice_service.service;

import com.business.invoice_service.dto.InvoiceResponse;
import com.business.invoice_service.dto.InvoiceResponseDTO;
import com.business.invoice_service.dto.UpdateBillDateRequest;
import com.business.invoice_service.entity.Invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    Invoice saveInvoice(Invoice invoiceRequest);

//    Invoice updateEndTime(Integer invoiceId, LocalDateTime endTime);

    List<InvoiceResponseDTO> getAllInvoices();

    //cập nhật endTime
    Invoice updateEndTimeByBookingId(Integer bookingId, LocalDateTime endTime);

//    Invoice updateInvoiceByBookingId(Integer bookingId, Invoice updatedInvoice);


    //cập nhật hóa đơn
    Invoice updateInvoice(Integer id, Invoice updatedInvoice);

    double getDailyRevenue(LocalDate date);
    double getMonthlyRevenue(LocalDate date);
    double getYearlyRevenue(LocalDate date);

    List<String> getRevenueLabels(LocalDate date);

    List<Double> getRevenueValues(LocalDate date);



    // Lấy hóa đơn theo bookingId
    Optional<Invoice> getInvoiceByBookingId(Integer bookingId);

    // Phương thức lấy số giờ chơi theo bookingId

//    Long getTotalPlaytimeHoursByBookingIdAndDate(Integer bookingId, LocalDate date);
//    Long getTotalPlaytimeForAllBookings(LocalDate date);

//    Long getTotalPlaytimeHoursByBookingId(Integer bookingId);
//    Long getPlaytimeHoursByBookingId(Integer bookingId);

//    Double calculateTotalPlayTime(LocalDate date);

    Integer calculateTotalPlayTime(LocalDate date);

    //tạo hóa đơn cho từng bàn trong booking
    void createInvoicesForBooking(Integer bookingId, List<Integer> tableIds);

    //lấy hóa đơn theo id
    InvoiceResponseDTO getInvoiceById(Integer id);

    //cập nhật endTime của bàn đc chọn (ấn nút Kết Thúc -> Chọn Bàn)
    Integer updateEndTimeAndLinkTable(Integer tableId, Integer bookingId, String endTime);


    //cập nhật hóa đơn (billDate, totalMoney) theo bàn
     Invoice updateInvoiceByTableId(Integer tableId, UpdateBillDateRequest request);

    List<Invoice> getInvoicesByBookingId(Integer bookingId);

//    Integer getInvoiceIdByTableId(Integer tableId);

//    InvoiceResponse findInvoiceByTableId(Integer tableId);

    InvoiceResponse findInvoiceByTableIdAndStatus(Integer tableId, String status);








//    Invoice getInvoiceByTableId(Integer tableId);
//    void createInvoiceForSelectedTable(Integer tableId, Integer bookingId, Double totalMoney);
//    void createInvoicesForBooking(Integer bookingId);
//    void createInvoicesForBooking(Integer bookingId, List<Integer> tableIds);

    Optional<Invoice> findById(Integer invoiceId);

    //booking có nhiều hóa đơn (có nhiều bàn)
//    List<Invoice> updateInvoiceTotalMoney(Integer bookingId, UpdateBillDateRequest update);
//    Invoice updateInvoiceTotalMoney(Integer bookingId, UpdateBillDateRequest update);
    //Invoice updateInvoiceTotalMoneyForTable(Integer invoiceId, Integer tableId, UpdateBillDateRequest update);
}
