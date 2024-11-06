package com.business.invoice_service.controller;

import com.business.invoice_service.dto.InvoiceResponseDTO;
import com.business.invoice_service.dto.UpdateBillDateRequest;
import com.business.invoice_service.dto.UpdateEndTimeRequest;
import com.business.invoice_service.entity.Invoice;
import com.business.invoice_service.repository.InvoiceRepo;
import com.business.invoice_service.service.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceRepo invoiceRepo;

    @PostMapping("/create")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoiceRequest) {
        try {
            // Lấy thời gian hiện tại
            LocalDateTime now = LocalDateTime.now();

            // Thiết lập các giá trị cần thiết cho hóa đơn mới
            invoiceRequest.setStartTime(now); // Thời gian bắt đầu chơi
            invoiceRequest.setEndTime(now); // Thời gian kết thúc hiện tại
            invoiceRequest.setBillDate(now); // Ngày lập hóa đơn
            invoiceRequest.setTotalMoney(0.0); // Số tiền mặc định là 0
            invoiceRequest.setStatus("Chưa Thanh Toán"); // Trạng thái mặc định

            // Lưu hóa đơn
            Invoice savedInvoice = invoiceService.saveInvoice(invoiceRequest);
            return ResponseEntity.ok(savedInvoice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
        try {
            List<InvoiceResponseDTO> invoices = invoiceService.getAllInvoices();
            return ResponseEntity.ok(invoices);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    // Cập nhật end_time cho hóa đơn
    @PutMapping("/update/byBookingId/{bookingId}/endTime")
    public ResponseEntity<Invoice> updateEndTimeByBookingId(@PathVariable Integer bookingId, @RequestBody UpdateEndTimeRequest request) {
        try {
            Invoice updatedInvoice = invoiceService.updateEndTimeByBookingId(bookingId, request.getEndTime());
            return ResponseEntity.ok(updatedInvoice);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/bill-totalMoney/{bookingId}")
    public ResponseEntity<?> updateInvoice(@PathVariable Integer bookingId, @RequestBody UpdateBillDateRequest update) {
        try {
            Invoice updatedInvoice = invoiceService.updateInvoiceTotalMoney(bookingId, update);
            return ResponseEntity.ok(updatedInvoice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInvoiceStatus(@PathVariable Integer id, @RequestBody Invoice updatedInvoice) {
        try {
            // Tìm hóa đơn theo ID
            Invoice invoice = invoiceRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Invoice not found for ID: " + id));

            // Cập nhật các trường cần thiết
            invoice.setStatus(updatedInvoice.getStatus());
//            invoice.setBillDate(updatedInvoice.getBillDate());
//            invoice.setTotalMoney(updatedInvoice.getTotalMoney());

            // Lưu lại hóa đơn đã cập nhật
            Invoice savedInvoice = invoiceRepo.save(invoice);

            return ResponseEntity.ok(savedInvoice);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating invoice: " + e.getMessage());
        }
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenue(@RequestParam("date") String dateStr) {
        // Kiểm tra định dạng ngày
        if (dateStr == null || dateStr.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Date is required"));
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);

            // Lấy doanh thu
            double dailyRevenue = invoiceService.getDailyRevenue(date);
            double monthlyRevenue = invoiceService.getMonthlyRevenue(date);
            double yearlyRevenue = invoiceService.getYearlyRevenue(date);

            // Lấy dữ liệu cho biểu đồ
            List<String> labels = invoiceService.getRevenueLabels(date); // Lấy nhãn
            List<Double> values = invoiceService.getRevenueValues(date); // Lấy giá trị

            // Tạo map để trả về dữ liệu
            Map<String, Object> response = new HashMap<>();
            response.put("daily", dailyRevenue);
            response.put("monthly", monthlyRevenue);
            response.put("yearly", yearlyRevenue);
            response.put("labels", labels); // Các nhãn cho biểu đồ
            response.put("values", values);  // Giá trị cho biểu đồ

            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid date format. Please use yyyy-MM-dd format."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while processing the request"));
        }
    }





}
