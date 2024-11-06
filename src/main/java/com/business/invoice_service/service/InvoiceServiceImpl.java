package com.business.invoice_service.service;

import com.business.invoice_service.dto.InvoiceResponseDTO;
import com.business.invoice_service.dto.UpdateBillDateRequest;
import com.business.invoice_service.entity.Invoice;
import com.business.invoice_service.exception.ResourceNotFoundException;
import com.business.invoice_service.repository.InvoiceRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService{
    @Autowired
    private InvoiceRepo invoiceRepo;

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepo.save(invoice);
    }

//    public Invoice updateEndTime(Integer invoiceId, LocalDateTime endTime) {
//        Invoice invoice = invoiceRepo.findById(invoiceId)
//                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
//
//        invoice.setEndTime(endTime); // Giả sử bạn đã có phương thức setEndTime trong Invoice
//        return invoiceRepo.save(invoice);
//    }

    public List<InvoiceResponseDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepo.findAll();
        return invoices.stream().map(invoice -> {
            InvoiceResponseDTO dto = new InvoiceResponseDTO();
            dto.setId(invoice.getId());
            dto.setStartTime(invoice.getStartTime());
            dto.setEndTime(invoice.getEndTime());
            dto.setBillDate(invoice.getBillDate());
            dto.setTotalMoney(invoice.getTotalMoney());
            dto.setStatus(invoice.getStatus());
            dto.setBookingId(invoice.getBookingId());

            return dto;
        }).collect(Collectors.toList());
    }

    public Invoice updateEndTimeByBookingId(Integer bookingId, LocalDateTime endTime) {
        Invoice invoice = invoiceRepo.findByBookingId(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found for bookingId: " + bookingId));
        invoice.setEndTime(endTime);
        return invoiceRepo.save(invoice);
    }

    public Invoice updateInvoiceTotalMoney(Integer bookingId, UpdateBillDateRequest update) {
        Invoice invoice = invoiceRepo.findByBookingId(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found for bookingId: " + bookingId));

        // Cập nhật các thông tin cần thiết
        invoice.setTotalMoney(update.getTotalMoney());
        invoice.setBillDate(update.getBillDate()); // Cập nhật ngày lập hóa đơn
        invoice.setStatus(update.getStatus());

        return invoiceRepo.save(invoice);
    }

    public Invoice updateInvoice(Integer id, Invoice updatedInvoice) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoice.setBillDate(updatedInvoice.getBillDate());
        invoice.setTotalMoney(updatedInvoice.getTotalMoney());
        invoice.setStatus(updatedInvoice.getStatus());

        return invoiceRepo.save(invoice);
    }

    public double getDailyRevenue(LocalDate date) {
        return invoiceRepo.getDailyRevenue(date);
    }

    public double getMonthlyRevenue(LocalDate date) {
        return invoiceRepo.getMonthlyRevenue(date);
    }

    public double getYearlyRevenue(LocalDate date) {
        return invoiceRepo.getYearlyRevenue(date);
    }

    public List<String> getRevenueLabels(LocalDate date) {
        List<String> labels = new ArrayList<>();
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        // Tạo danh sách nhãn từ ngày đầu tháng đến ngày cuối tháng
        for (LocalDate currentDate = startOfMonth; !currentDate.isAfter(endOfMonth); currentDate = currentDate.plusDays(1)) {
            labels.add(currentDate.toString()); // Định dạng ngày dưới dạng chuỗi
        }

        return labels;
    }

    public List<Double> getRevenueValues(LocalDate date) {
        List<Double> values = new ArrayList<>();
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        // Giả sử bạn có phương thức để lấy doanh thu cho từng ngày
        for (LocalDate currentDate = startOfMonth; !currentDate.isAfter(endOfMonth); currentDate = currentDate.plusDays(1)) {
            double dailyRevenue = getDailyRevenue(currentDate);
            values.add(dailyRevenue);
        }

        return values;
    }

}
