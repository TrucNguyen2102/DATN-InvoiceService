//package com.business.invoice_service.service;
//
//import com.business.invoice_service.entity.InvoiceTable;
//import com.business.invoice_service.repository.InvoiceTableRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class InvoiceTableServiceImpl implements InvoiceTableService{
//
//    @Autowired
//    private InvoiceTableRepo invoiceTableRepo;
//    // Tìm InvoiceTable theo tableId
//    public Optional<InvoiceTable> findByTableId(Integer tableId) {
//        return invoiceTableRepo.findByTableId(tableId);
//    }
//
//    public InvoiceTable findTableByInvoiceId(Integer invoiceId) {
//        return invoiceTableRepo.findByInvoiceId(invoiceId).orElse(null);
//    }
//
//}
