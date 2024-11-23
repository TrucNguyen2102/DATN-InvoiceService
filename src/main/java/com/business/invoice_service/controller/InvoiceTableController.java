//package com.business.invoice_service.controller;
//
//import com.business.invoice_service.entity.InvoiceTable;
//import com.business.invoice_service.service.InvoiceTableService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/invoices")
//public class InvoiceTableController {
//
//    @Autowired
//    private InvoiceTableService invoiceTableService;
//
//    // API lấy thông tin InvoiceTable dựa trên tableId
//    @GetMapping("/invoice_table/{tableId}")
//    public ResponseEntity<InvoiceTable> getInvoiceTableByTableId(@PathVariable Integer tableId) {
//        try {
//            System.out.println("Tìm kiếm hóa đơn cho bàn với tableId: " + tableId);
//            Optional<InvoiceTable> invoiceTable = invoiceTableService.findByTableId(tableId);
//            if (invoiceTable.isPresent()) {
//                System.out.println("Tìm thấy hóa đơn: " + invoiceTable.get().getInvoice().getId());
//                return ResponseEntity.ok(invoiceTable.get());
//            } else {
//                System.out.println("Không tìm thấy hóa đơn cho bàn với tableId: " + tableId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
