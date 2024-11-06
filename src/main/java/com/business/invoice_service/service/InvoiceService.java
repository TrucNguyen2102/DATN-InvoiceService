package com.business.invoice_service.service;

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

    Invoice updateEndTimeByBookingId(Integer bookingId, LocalDateTime endTime);

//    Invoice updateInvoiceByBookingId(Integer bookingId, Invoice updatedInvoice);

    Invoice updateInvoiceTotalMoney(Integer bookingId, UpdateBillDateRequest update);

    Invoice updateInvoice(Integer id, Invoice updatedInvoice);

    double getDailyRevenue(LocalDate date);
    double getMonthlyRevenue(LocalDate date);
    double getYearlyRevenue(LocalDate date);

    List<String> getRevenueLabels(LocalDate date);

    List<Double> getRevenueValues(LocalDate date);


}
