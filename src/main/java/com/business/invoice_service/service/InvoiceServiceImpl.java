package com.business.invoice_service.service;

import com.business.invoice_service.dto.*;
import com.business.invoice_service.entity.Invoice;

import com.business.invoice_service.exception.ResourceNotFoundException;
import com.business.invoice_service.repository.InvoiceRepo;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService{
    @Autowired
    private InvoiceRepo invoiceRepo;

//    @Autowired
//    private InvoiceTableRepo invoiceTableRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tablePlayServiceUrl}") // URL của TablePlay Service
    private String tablePlayServiceUrl;

//    @Value("${bookingServiceUrl}") // URL của TablePlay Service
//    private String bookingServiceUrl;


    public InvoiceServiceImpl(InvoiceRepo invoiceRepo, RestTemplate restTemplate, @Value("${tablePlayServiceUrl}") String tablePlayServiceUrl) {
        this.invoiceRepo = invoiceRepo;
        this.restTemplate = restTemplate;
        this.tablePlayServiceUrl = tablePlayServiceUrl;
    }

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

    //hiển thị ds hóa đơn
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
            dto.setTableId(invoice.getTableId());

            return dto;
        }).collect(Collectors.toList());
    }

    //cập nhật endTime
    public Invoice updateEndTimeByBookingId(Integer bookingId, LocalDateTime endTime) {
        Invoice invoice = invoiceRepo.findByBookingId(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found for bookingId: " + bookingId));
        invoice.setEndTime(endTime);
        return invoiceRepo.save(invoice);
    }

    //cập nhật hóa đơn (billDate, totalMoney) theo bàn
//    public Invoice updateInvoiceByTableId(Integer tableId, UpdateBillDateRequest request) {
//        // Tìm hóa đơn liên quan đến tableId
//        Invoice invoice = invoiceRepo.findByTableId(tableId)
//                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn cho bàn ID: " + tableId));
//
//        // Nếu hóa đơn đã thanh toán, không cho phép thay đổi tổng tiền
//        if (invoice.getStatus().equals("Đã Thanh Toán")) {
//            // Không cho phép thay đổi tổng tiền của hóa đơn đã thanh toán
//            if (request.getTotalMoney() != null && request.getTotalMoney() != invoice.getTotalMoney()) {
//                throw new IllegalStateException("Không thể thay đổi tổng tiền của hóa đơn đã thanh toán.");
//            }
//
//            // Cập nhật các trường khác (nếu cần thiết, ví dụ: billDate)
//            invoice.setBillDate(request.getBillDate());
//        } else if (invoice.getStatus().equals("Chưa Thanh Toán")) {
//            // Nếu hóa đơn chưa thanh toán, có thể cập nhật toàn bộ các thông tin
//            invoice.setTotalMoney(request.getTotalMoney());
//            invoice.setBillDate(request.getBillDate());
//            invoice.setStatus(request.getStatus());
//        }
//
//        // Lưu lại hóa đơn vào database
//        return invoiceRepo.save(invoice);
//    }


    public Invoice updateInvoiceByTableId(Integer tableId, UpdateBillDateRequest request) {
        // Chỉ lấy hóa đơn chưa thanh toán hoặc hóa đơn mới nhất
        Invoice invoice = invoiceRepo.findTopByTableIdAndStatusOrderByBillDateDesc(tableId, "Chưa Thanh Toán")
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn cho bàn ID: " + tableId));
        // Cập nhật thông tin hóa đơn
        invoice.setTotalMoney(request.getTotalMoney());
        invoice.setBillDate(request.getBillDate());
        invoice.setStatus(request.getStatus());

        // Lưu lại hóa đơn vào database
        return invoiceRepo.save(invoice);
    }

//    public Invoice updateInvoiceTotalMoney(Integer bookingId, UpdateBillDateRequest update) {
//        Invoice invoice = invoiceRepo.findByBookingId(bookingId)
//                .orElseThrow(() -> new EntityNotFoundException("Invoice not found for bookingId: " + bookingId));
//
//        // Cập nhật các thông tin cần thiết
//        invoice.setTotalMoney(update.getTotalMoney());
//        invoice.setBillDate(update.getBillDate()); // Cập nhật ngày lập hóa đơn
//        invoice.setStatus(update.getStatus());
//
//        return invoiceRepo.save(invoice);
//    }

//    public Invoice updateInvoiceTotalMoney(Integer tableId, UpdateBillDateRequest update) {
//        // Lấy hóa đơn dựa trên tableId từ bảng InvoiceTable
//        InvoiceTable invoiceTable = invoiceTableRepo.findByTableId(tableId)
//                .orElseThrow(() -> new EntityNotFoundException("Invoice not found for tableId: " + tableId));
//
//        Invoice invoice = invoiceTable.getInvoice(); // Lấy thông tin hóa đơn từ InvoiceTable
//
//        // Cập nhật các thông tin cần thiết
//        invoice.setTotalMoney(update.getTotalMoney());
//        invoice.setBillDate(update.getBillDate()); // Cập nhật ngày lập hóa đơn
//        invoice.setStatus(update.getStatus());
//
//        return invoiceRepo.save(invoice);
//    }





//    public Invoice updateInvoiceTotalMoneyForTable(Integer bookingId, Integer tableId, UpdateBillDateRequest update) {
//        // Tìm InvoiceTable dựa trên bookingId và tableId
//        Optional<InvoiceTable> invoiceTableOptional = invoiceTableRepo.findByBookingIdAndTableId(bookingId, tableId);
//
//        if (!invoiceTableOptional.isPresent()) {
//            throw new EntityNotFoundException("Không tìm thấy hóa đơn cho bàn với bookingId: " + bookingId + " và tableId: " + tableId);
//        }
//
//        // Lấy hóa đơn từ liên kết bảng InvoiceTable
//        InvoiceTable invoiceTable = invoiceTableOptional.get();
//        Invoice invoice = invoiceTable.getInvoice();
//
//        // Cập nhật các thông tin cần thiết
//        invoice.setTotalMoney(update.getTotalMoney());
//        invoice.setBillDate(update.getBillDate());
//        invoice.setStatus(update.getStatus());
//
//        // Lưu lại hóa đơn đã cập nhật
//        return invoiceRepo.save(invoice);
//    }

    //cập nhật hóa đơn
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

    // Lấy hóa đơn theo bookingId
    public Optional<Invoice> getInvoiceByBookingId(Integer bookingId) {
        return invoiceRepo.findByBookingId(bookingId);
    }

    // Phương thức lấy số giờ chơi theo bookingId

    // Tính tổng thời gian chơi của các bàn trong ngày

//    public Double calculateTotalPlayTime(LocalDate date) {
//        // Tạo thời gian bắt đầu và kết thúc cho ngày đã cho
//        LocalDateTime startOfDay = date.atStartOfDay();  // Thời gian 00:00:00
//        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999); // Thời gian 23:59:59.999999999
//
//        // Lấy tất cả các hóa đơn trong khoảng thời gian này
//        List<Invoice> invoices = invoiceRepo.findByBillDateBetween(startOfDay, endOfDay);
//
//        double totalPlayTime = 0.0;
//        for (Invoice invoice : invoices) {
//            LocalDateTime startTime = invoice.getStartTime();
//            LocalDateTime endTime = invoice.getEndTime();
//
//            // Tính thời gian chơi
//            if (startTime != null && endTime != null) {
//                Duration duration = Duration.between(startTime, endTime);
//                totalPlayTime += duration.toHours(); // Thêm vào tổng số giờ
//            }
//        }
//
//        return totalPlayTime;
//    }

    public Integer calculateTotalPlayTime(LocalDate date) {
        Integer totalPlayTime = invoiceRepo.calculateTotalPlayTimeInMinutes(date);
        return totalPlayTime != null ? totalPlayTime : 0; // Trả về 0 nếu không có dữ liệu
    }



//    public Double calculateTotalPlayTime(LocalDate date) {
//        List<Invoice> invoices = invoiceRepo.findByBillDate(date); // Tìm hóa đơn theo ngày
//
//        double totalPlayTime = 0.0;
//        for (Invoice invoice : invoices) {
//            LocalDateTime startTime = invoice.getStartTime();
//            LocalDateTime endTime = invoice.getEndTime();
//
//            // Tính thời gian chơi bằng cách trừ startTime khỏi endTime
//            if (startTime != null && endTime != null) {
//                Duration duration = Duration.between(startTime, endTime);
//                totalPlayTime += duration.toHours(); // Thêm vào tổng số giờ
//            }
//        }
//
//        return totalPlayTime;
//    }

//    public Long getTotalPlaytimeHoursByBookingIdAndDate(Integer bookingId, LocalDate date) {
//        // Lấy danh sách các Invoice theo bookingId và ngày
//        List<Invoice> invoices = invoiceRepo.findAllByBookingIdAndDate(bookingId, date);
//
//        // Nếu không có Invoice nào, trả về 0
//        if (invoices.isEmpty()) {
//            System.out.println("Không có hóa đơn nào cho bookingId: " + bookingId + " và ngày: " + date);
//            return 0L;
//        }
//
//        // Tính tổng số giờ chơi
//        long totalHours = 0;
//        for (Invoice invoice : invoices) {
//            if (invoice.getStartTime() != null && invoice.getEndTime() != null) {
//                // Tính thời gian chơi cho từng hóa đơn
//                Duration duration = Duration.between(invoice.getStartTime(), invoice.getEndTime());
//                totalHours += duration.toHours();
//            } else {
//                System.out.println("Hóa đơn thiếu startTime hoặc endTime: Invoice ID = " + invoice.getId());
//            }
//        }
//
//        return totalHours; // Trả về tổng số giờ chơi
//    }


//    public Long getTotalPlaytimeHoursByBookingId(Integer bookingId) {
//        // Tìm tất cả các Invoice liên quan đến bookingId
//        List<Invoice> invoices = invoiceRepo.findAllByBookingId(bookingId);
//
//        // Nếu không có Invoice nào, trả về 0
//        if (invoices.isEmpty()) {
//            System.out.println("Không có hóa đơn nào cho bookingId: " + bookingId);
//            return 0L;
//        }
//
//        // Tính tổng số giờ chơi
//        long totalHours = 0;
//        for (Invoice invoice : invoices) {
//            if (invoice.getStartTime() != null && invoice.getEndTime() != null) {
//                // Tính thời gian chơi cho từng hóa đơn
//                Duration duration = Duration.between(invoice.getStartTime(), invoice.getEndTime());
//                totalHours += duration.toHours();
//            } else {
//                System.out.println("Hóa đơn thiếu startTime hoặc endTime: Invoice ID = " + invoice.getId());
//            }
//        }
//
//        return totalHours; // Trả về tổng số giờ chơi
//    }

//    public Long getPlaytimeHoursByBookingId(Integer bookingId) {
//        // Tìm Invoice dựa trên bookingId
//        Optional<Invoice> optionalInvoice = invoiceRepo.findByBookingId(bookingId);
//
//        // Kiểm tra xem có Invoice không
//        if (optionalInvoice.isPresent()) {
//            Invoice invoice = optionalInvoice.get();
//
//            // Kiểm tra nếu cả startTime và endTime đều có giá trị
//            if (invoice.getStartTime() != null && invoice.getEndTime() != null) {
//                // Tính toán số giờ chơi
//                Duration duration = Duration.between(invoice.getStartTime(), invoice.getEndTime());
//                long hours = duration.toHours(); // Lấy số giờ
//                return hours;  // Trả về số giờ chơi
//            } else {
//                // Trường hợp không có startTime hoặc endTime
//                System.out.println("Start time hoặc End time không có trong invoice.");
//            }
//        } else {
//            // Trường hợp không tìm thấy Invoice với bookingId
//            System.out.println("Không tìm thấy invoice với bookingId: " + bookingId);
//        }
//
//        return 0L; // Trả về 0 nếu không có số giờ chơi
//    }

    //tạo hóa đơn cho từng bàn trong booking
    public void createInvoicesForBooking(Integer bookingId, List<Integer> tableIds) {
        if (tableIds == null || tableIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách bàn không hợp lệ.");
        }

        for (Integer tableId : tableIds) {
            Invoice invoice = new Invoice();
            invoice.setBookingId(bookingId);
            invoice.setStartTime(LocalDateTime.now()); // Set thời gian bắt đầu
            invoice.setEndTime(null);
            invoice.setBillDate(null); // Chưa tạo hóa đơn thanh toán
            invoice.setTotalMoney(0.0); // Số tiền sẽ được cập nhật sau
            invoice.setStatus("Chưa Thanh Toán");
            invoice.setTableId(tableId);
            invoiceRepo.save(invoice);
        }
    }
    //lấy hóa đơn theo id
    public InvoiceResponseDTO getInvoiceById(Integer id) {
        Optional<Invoice> optionalInvoice = invoiceRepo.findById(id);

        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            InvoiceResponseDTO dto = new InvoiceResponseDTO();
            dto.setId(invoice.getId());
            dto.setStartTime(invoice.getStartTime());
            dto.setEndTime(invoice.getEndTime());
            dto.setBillDate(invoice.getBillDate());
            dto.setTotalMoney(invoice.getTotalMoney());
            dto.setStatus(invoice.getStatus());
            dto.setBookingId(invoice.getBookingId());
            dto.setTableId(invoice.getTableId());

            return dto;
        }

        return null; // Không tìm thấy hóa đơn
    }

    // Cập nhật thời gian kết thúc cho hóa đơn dựa trên tableId
//    public Integer updateEndTimeAndLinkTable(Integer tableId, Integer bookingId, String endTime) {
//        // Tìm hóa đơn ban đầu liên kết với booking
//        Invoice invoice = invoiceRepo.findByBookingId(bookingId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn cho booking với ID: " + bookingId));
//
//        // Cập nhật thời gian kết thúc cho hóa đơn
//        try {
//            invoice.setEndTime(LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        } catch (DateTimeParseException e) {
//            throw new RuntimeException("Định dạng thời gian không hợp lệ: " + endTime);
//        }
//
//        invoiceRepo.save(invoice);
//
//        // Lưu liên kết giữa hóa đơn và bàn vào bảng InvoiceTable
//        InvoiceTable invoiceTable = new InvoiceTable();
//        InvoiceTableId invoiceTableId = new InvoiceTableId();
//        invoiceTableId.setInvoiceId(invoice.getId());
//        invoiceTableId.setTableId(tableId);
//
//        invoiceTable.setId(invoiceTableId);
//        invoiceTable.setInvoice(invoice);
//        invoiceTable.setTableId(tableId);
//
//        invoiceTableRepo.save(invoiceTable);
//
//        // Trả về ID của hóa đơn để lưu vào database
//        return invoice.getId();
//    }

    public Integer updateEndTimeAndLinkTable(Integer tableId, Integer bookingId, String endTime) {
        // Tìm tất cả hóa đơn liên kết với bookingId
        List<Invoice> invoices = invoiceRepo.findAllByBookingId(bookingId);

        // Kiểm tra nếu không tìm thấy hóa đơn nào
        if (invoices.isEmpty()) {
            throw new RuntimeException("Không tìm thấy hóa đơn cho booking với ID: " + bookingId);
        }

        // Kiểm tra bàn có liên kết với booking không, nếu không, báo lỗi
        Invoice invoice = null;
        for (Invoice inv : invoices) {
            if (inv.getTableId() != null && inv.getTableId().equals(tableId)) {
                invoice = inv;
                break; // Dừng vòng lặp khi tìm thấy hóa đơn liên kết với bàn
            }
        }

        // Nếu không tìm thấy hóa đơn nào liên kết với bàn, báo lỗi
        if (invoice == null) {
            throw new RuntimeException("Không tìm thấy hóa đơn liên kết với bàn " + tableId + " cho booking với ID: " + bookingId);
        }

        // Cập nhật thời gian kết thúc cho hóa đơn
        try {
            invoice.setEndTime(LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Định dạng thời gian không hợp lệ: " + endTime);
        }

        // Lưu hóa đơn đã cập nhật
        invoiceRepo.save(invoice);

        // Trả về ID của hóa đơn đã cập nhật
        return invoice.getId();
    }



    // Lấy hóa đơn theo tableId
//    public Invoice getInvoiceByTableId(Integer tableId) {
//        InvoiceTable invoiceTable = invoiceTableRepo.findByTableId(tableId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy InvoiceTable với tableId: " + tableId));
//        return invoiceRepo.findById(invoiceTable.getInvoice().getId())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với tableId: " + tableId));
//    }


//    public void createInvoicesForBooking(Integer bookingId) {
//        // Gọi API từ BookingTable Service để lấy danh sách tableIds
//        try {
//        String url = bookingServiceUrl + "/booking_table/" + bookingId + "/tables";
//        System.out.println("URL gọi API: " + url);
//
//
//            // Gọi API và nhận danh sách các BookingTableDTO
//            ResponseEntity<List<BookingTableDTO>> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<BookingTableDTO>>() {}
//            );
//
//            // Kiểm tra trạng thái HTTP của phản hồi
//            if (response.getStatusCode().is2xxSuccessful()) {
//                List<BookingTableDTO> bookingTables = response.getBody();
//                System.out.println("Received booking tables: " + bookingTables);
//
//                // Kiểm tra dữ liệu trả về không null hoặc rỗng
//                if (bookingTables == null || bookingTables.isEmpty()) {
//                    throw new RuntimeException("Danh sách tableIds trả về từ BookingTable Service bị null hoặc rỗng.");
//                }
//
//                // Lấy tất cả tableIds từ từng BookingTableDTO
//                List<Integer> tableIds = bookingTables.stream()
//                        .flatMap(bookingTableDTO -> bookingTableDTO.getTableIds().stream())  // Lấy tất cả các tableId từ danh sách tableIds
//                        .collect(Collectors.toList());
//
//                // Tạo hóa đơn
//                Invoice invoice = new Invoice();
//                invoice.setBookingId(bookingId);
//                invoice.setStartTime(LocalDateTime.now());
//                invoice.setBillDate(LocalDateTime.now());
//                invoice.setTotalMoney(0.0);
//                invoice.setStatus("Chưa Thanh Toán");
//
//                // Lưu Invoice vào cơ sở dữ liệu
//                Invoice savedInvoice = invoiceRepo.save(invoice);
//
//                // Lưu thông tin vào InvoiceTable
//                for (Integer tableId : tableIds) {
//                    InvoiceTable invoiceTable = new InvoiceTable();
//
//                    // Thiết lập ID hỗn hợp
//                    InvoiceTableId invoiceTableId = new InvoiceTableId();
//                    invoiceTableId.setInvoiceId(savedInvoice.getId());
//                    invoiceTableId.setTableId(tableId);
//
//                    invoiceTable.setId(invoiceTableId);
//                    invoiceTable.setInvoice(savedInvoice);
//                    invoiceTable.setTableId(tableId);
//
//                    // Lưu vào bảng InvoiceTable
//                    invoiceTableRepo.save(invoiceTable);
//
//                    System.out.println("Đã lưu vào InvoiceTable: InvoiceId = " + savedInvoice.getId() + ", TableId = " + tableId);
//                }
//
//            } else {
//                // Ném lỗi khi dịch vụ trả về trạng thái không thành công
//                throw new RuntimeException("Dịch vụ BookingTable trả về mã trạng thái: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Lỗi khác: " + e.getMessage());
//            throw new RuntimeException("Lỗi khi tạo hóa đơn: " + e.getMessage());
//        }
//    }

//    public void createInvoicesForBooking(Integer bookingId, List<Integer> tableIds) {
//        // Lặp qua từng bàn trong danh sách
//        for (Integer tableId : tableIds) {
//            // Tạo hóa đơn mới
//            Invoice invoice = new Invoice();
//            invoice.setBookingId(bookingId);
//            invoice.setStartTime(LocalDateTime.now());
//            invoice.setEndTime(LocalDateTime.now()); //mặc định ban đầu là LocalDateTime.now()
//            invoice.setBillDate(LocalDateTime.now()); //mặc định ban đầu là LocalDateTime.now()
//            invoice.setTotalMoney(0.0);
//            invoice.setStatus("Chưa Thanh Toán");
//            invoiceRepo.save(invoice);
//
//            // Tạo InvoiceTable để lưu mối quan hệ
//            InvoiceTable invoiceTable = new InvoiceTable();
//            InvoiceTableId id = new InvoiceTableId(invoice.getId(), tableId);
//            invoiceTable.setId(id);
//            invoiceTable.setInvoice(invoice);
//
//            invoiceTableRepo.save(invoiceTable);
//        }
//    }

    // Tìm hóa đơn theo invoiceId
    public Optional<Invoice> findById(Integer invoiceId) {
        return invoiceRepo.findById(invoiceId);
    }

    public List<Invoice> getInvoicesByBookingId(Integer bookingId) {
        return invoiceRepo.findInvoiceByBookingId(bookingId);
    }

    // Phương thức để lấy invoiceId từ tableId
//    public Integer getInvoiceIdByTableId(Integer tableId) {
//        // URL của Service Table (thay đổi cho đúng địa chỉ)
//        String tableServiceUrl = tablePlayServiceUrl + "/" + tableId;
//
//        // Gọi API từ Table Service để lấy thông tin bàn
//        ResponseEntity<TablePlayDTO> response = restTemplate.exchange(tableServiceUrl, HttpMethod.GET, null, TablePlayDTO.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            TablePlayDTO tableDTO = response.getBody();
//            if (tableDTO != null) {
//                // Tìm hóa đơn có trạng thái "Chưa Thanh Toán" cho bàn
//                Invoice invoice = invoiceRepo.findByTableIdAndStatus(tableId, "Chưa Thanh Toán")
//                        .orElseThrow(() -> new RuntimeException("No active invoice found for table id: " + tableId));
//                return invoice.getId(); // Trả về invoiceId
//            }
//        } else {
//            throw new RuntimeException("Error calling Table Service");
//        }
//
//        return null; // If no invoice is found
//    }

    // Tìm hóa đơn dựa trên tableId
    // Tìm hóa đơn của bàn có trạng thái "Chưa Thanh Toán"
    public InvoiceResponse findInvoiceByTableIdAndStatus(Integer tableId, String status) {
        Optional<Invoice> invoice = invoiceRepo.findByTableIdAndStatus(tableId, status);

        if (invoice.isPresent()) {
            // Trả về dữ liệu hóa đơn nếu tìm thấy
            return new InvoiceResponse(invoice.get().getId());
        } else {
            // Trả về null hoặc exception nếu không tìm thấy hóa đơn
            return null;
        }
    }


}
