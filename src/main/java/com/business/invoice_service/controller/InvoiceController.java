package com.business.invoice_service.controller;

import com.business.invoice_service.dto.*;
import com.business.invoice_service.entity.Invoice;
//import com.business.invoice_service.entity.InvoiceTable;
import com.business.invoice_service.repository.InvoiceRepo;
import com.business.invoice_service.service.InvoiceService;
//import com.business.invoice_service.service.InvoiceTableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
//    @Autowired
//    private InvoiceTableService invoiceTableService;
    @Autowired
    private InvoiceRepo invoiceRepo;

    @GetMapping("/endpoints")
    public List<Map<String, String>> getEndpoints() {
        return List.of(
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/revenue"),
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/total-playtime"),
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/all"),
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/byTableIdAndStatus/{tableId}/{status}"),
                Map.of("service", "invoice-service", "method", "POST", "url", "/api/invoices/create-for-booking/{id}"),
                Map.of("service", "invoice-service", "method", "PUT", "url", "/api/invoices/update/byBookingId/{id}/endTime"),
                Map.of("service", "invoice-service", "method", "PUT", "url", "/api/invoices/updateEndTimeAndLinkTable/{tableId}"),
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/{id}"),
                Map.of("service", "invoice-service", "method", "PUT", "url", "/api/invoices/update/bill-totalMoney/{tableId}"),
                Map.of("service", "invoice-service", "method", "PUT", "url", "/api/invoices/update/{id}"),
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/check-table-used/{tableId}"),
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/count-by-payment-method"),
                Map.of("service", "invoice-service", "method", "GET", "url", "/api/invoices/total-by-payment-method")
        );
    }

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

    //tạo hóa đơn cho từng bàn trong booking (chức năng Nhận Bàn)
    @PostMapping("/create-for-booking/{bookingId}")
    public ResponseEntity<String> createInvoicesForBooking(
            @PathVariable Integer bookingId,
            @RequestBody List<Integer> tableIds) {
        try {
            if (tableIds == null || tableIds.isEmpty()) {
                return ResponseEntity.badRequest().body("Danh sách bàn không hợp lệ.");
            }

            invoiceService.createInvoicesForBooking(bookingId, tableIds);
            return ResponseEntity.ok("Đã tạo hóa đơn cho từng bàn thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo hóa đơn: " + e.getMessage());
        }
    }



    // API để tạo hóa đơn cho một bàn được yêu cầu thanh toán
//    @PostMapping("/createForSelectedTable")
//    public ResponseEntity<String> createInvoiceForSelectedTable(
//            @RequestParam Integer tableId,
//            @RequestParam Integer bookingId,
//            @RequestParam Double totalMoney) {
//        try {
//            invoiceService.createInvoiceForSelectedTable(tableId, bookingId, totalMoney);
//            return ResponseEntity.ok("Hóa đơn đã được tạo cho bàn: " + tableId);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + e.getMessage());
//        }
//    }

//    @PostMapping("/create-for-booking/{bookingId}")
//    public ResponseEntity<String> createInvoicesForBooking(
//            @PathVariable Integer bookingId,
//            @RequestBody List<Integer> tableIds) {
//        try {
//            if (tableIds == null || tableIds.isEmpty()) {
//                return ResponseEntity.badRequest().body("Danh sách bàn không hợp lệ.");
//            }
//
//            invoiceService.createInvoicesForBooking(bookingId, tableIds);
//            return ResponseEntity.ok("Đã tạo hóa đơn cho từng bàn thành công.");
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//    }

//    public ResponseEntity<String> createInvoicesForBooking(@PathVariable Integer bookingId) {
//        try {
//            invoiceService.createInvoicesForBooking(bookingId);
//            return ResponseEntity.ok("Đã tạo hóa đơn và lưu vào InvoiceTable thành công!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Lỗi khi tạo hóa đơn và lưu vào InvoiceTable.");
//        }
//    }


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

    // Lấy danh sách hóa đơn theo bookingId
//    @GetMapping("/booking/{bookingId}")
//    public ResponseEntity<Invoice> getInvoiceByBookingId(@PathVariable Integer bookingId) {
//        try {
//            Optional<Invoice> invoice = invoiceService.getInvoiceByBookingId(bookingId);
//
//            if (invoice.isPresent()) {
//                return ResponseEntity.ok(invoice.get()); // Trả về hóa đơn nếu có
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(null); // Trả về 404 nếu không tìm thấy hóa đơn
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//
//    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Invoice>> getInvoicesByBookingId(@PathVariable Integer bookingId) {
        try {
            List<Invoice> invoices = invoiceService.getInvoicesByBookingId(bookingId);

            if (!invoices.isEmpty()) {
                return ResponseEntity.ok(invoices); // Trả về tất cả hóa đơn
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Không tìm thấy hóa đơn
            }
        } catch (Exception e) {
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

//    @PutMapping("/update/bill-totalMoney/{bookingId}")
//    public ResponseEntity<?> updateInvoiceForBooking(@PathVariable Integer bookingId, @RequestBody UpdateBillDateRequest update) {
//        try {
//            Invoice updatedInvoice = invoiceService.updateInvoiceTotalMoney(bookingId, update);
//            return ResponseEntity.ok(updatedInvoice);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Có lỗi xảy ra khi cập nhật hóa đơn: " + e.getMessage());
//        }
//    }


    //cập nhật hóa đơn của bàn khi lưu hóa đơn (chức năng Tạo Hóa Đơn -> Lưu)
    @PutMapping("/update/bill-totalMoney/{tableId}")
    public ResponseEntity<?> updateInvoiceByTable(
            @PathVariable Integer tableId,
            @RequestBody UpdateBillDateRequest request) {
        try {
            // Gọi service để cập nhật hóa đơn theo tableId
            Invoice updatedInvoice = invoiceService.updateInvoiceByTableId(tableId, request);
            return ResponseEntity.ok(updatedInvoice);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn cho bàn ID: " + tableId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
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
            invoice.setMethodId(updatedInvoice.getMethodId());
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


    @GetMapping("/revenue/range")
    public ResponseEntity<Map<String, Object>> getRevenueRange(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {

        // Kiểm tra định dạng ngày
        if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Start date and end date are required"));
        }

        try {
            // Chuyển đổi chuỗi ngày thành LocalDate
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Lấy tổng doanh thu trong khoảng thời gian từ startDate đến endDate
            double totalRevenue = invoiceService.getTotalRevenueInRange(startDate, endDate);

            // Trả về tổng doanh thu
            Map<String, Object> response = new HashMap<>();
            response.put("totalRevenue", totalRevenue);

            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            // Xử lý lỗi định dạng ngày
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid date format. Please use yyyy-MM-dd format."));
        } catch (Exception e) {
            // Xử lý các lỗi khác
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while processing the request"));
        }
    }

    // API lấy dữ liệu cho biểu đồ doanh thu
    @GetMapping("/chart-data")
    public ResponseEntity<Map<String, Object>> getChartData(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // Chuyển đổi từ String sang LocalDate
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            // Chuyển startDate và endDate sang LocalDateTime để tính cả ngày
            LocalDateTime startDateTime = start.atStartOfDay(); // Từ 00:00:00 của ngày bắt đầu
            LocalDateTime endDateTime = end.atTime(23, 59, 59); // Đến 23:59:59 của ngày kết thúc

            // Lấy dữ liệu cho biểu đồ (doanh thu theo ngày)
            List<Object[]> chartData = invoiceService.getChartData(startDateTime, endDateTime);

            // Tạo dữ liệu cho biểu đồ
            List<String> labels = new ArrayList<>();
            List<Double> values = new ArrayList<>();
            for (Object[] data : chartData) {
                labels.add(data[0].toString());  // Ngày
                values.add((Double) data[1]);    // Doanh thu
            }

            Map<String, Object> response = new HashMap<>();
            response.put("labels", labels);
            response.put("values", values);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




//    @GetMapping("/total-playtime")
//    public ResponseEntity<Integer> getTotalPlayTime(@RequestParam("date") String dateStr) {
//        try {
//            LocalDate date = LocalDate.parse(dateStr);
//            int totalMinutes = invoiceService.calculateTotalPlayTime(date);
//            return ResponseEntity.ok(totalMinutes); // Trả về tổng số phút
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//    }

    @GetMapping("/total-playtime")
    public ResponseEntity<Map<String, Integer>> getTotalPlayTime(@RequestParam("startDate") String startDateStr,
                                                                 @RequestParam("endDate") String endDateStr) {
        try {
            // Chuyển đổi từ String sang LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(startDateStr + " 00:00:00", formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDateStr + " 23:59:59", formatter);

            // Gọi service để tính tổng thời gian chơi
            Integer totalMinutes = invoiceService.calculateTotalPlayTime(startDateTime, endDateTime);

            // Trả về kết quả
            Map<String, Integer> response = new HashMap<>();
            response.put("totalMinutes", totalMinutes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }













    @GetMapping("/byTableIdAndStatus/{tableId}/{status}")
    public ResponseEntity<InvoiceResponse> getInvoiceByTableIdAndStatus(
            @PathVariable Integer tableId,
            @PathVariable String status) {

        try {
            InvoiceResponse invoiceResponse = invoiceService.findInvoiceByTableIdAndStatus(tableId, status);

            if (invoiceResponse != null) {
                return ResponseEntity.ok(invoiceResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping("/updateEndTimeAndLinkTable/{tableId}")
    public ResponseEntity<String> updateEndTimeAndLinkTable(@PathVariable Integer tableId,
                                                            @RequestParam Integer bookingId,
                                                            @RequestParam String endTime) {
        try {
            Integer invoiceId = invoiceService.updateEndTimeAndLinkTable(tableId, bookingId, endTime);
            return ResponseEntity.ok("Hóa đơn đã được cập nhật thời gian kết thúc và liên kết với bàn: " + tableId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // API lấy thông tin hóa đơn theo id
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Integer id) {
        try {
            InvoiceResponseDTO invoice = invoiceService.getInvoiceById(id);
            if (invoice != null) {
                return ResponseEntity.ok(invoice);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // API kiểm tra bàn có trong hóa đơn nào không
    @GetMapping("/check-table-used/{tableId}")
    public ResponseEntity<Boolean> isTableUsedInInvoice(@PathVariable Integer tableId) {
        try {
            // Kiểm tra xem có Invoice nào chứa tableId này không
            boolean isUsed = invoiceRepo.existsByTableId(tableId);

            // Trả về kết quả
            return ResponseEntity.ok(isUsed);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/count-by-payment-method")
    public ResponseEntity<Map<String, Long>> countInvoicesByPaymentMethod() {
        try {
            Map<String, Long> stats = invoiceService.countInvoicesByPaymentMethod();
            return ResponseEntity.ok(stats);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/total-by-payment-method")
    public ResponseEntity<Map<String, Double>> totalInvoicesByPaymentMethod() {
        try {
            Map<String, Double> stats = invoiceService.totalInvoicesByPaymentMethod();
            return ResponseEntity.ok(stats);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

//    @GetMapping("/total-by-payment-method")
//    public ResponseEntity<List<PaymentDTO>> getPaymentStats() {
//        try {
//            List<PaymentDTO> stats = invoiceService.getPaymentStats();
//            return ResponseEntity.ok(stats);
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//    }







}
