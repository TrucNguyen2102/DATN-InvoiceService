//package com.business.invoice_service.repository;
//
//import com.business.invoice_service.entity.InvoiceTable;
//import com.business.invoice_service.entity.InvoiceTableId;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface InvoiceTableRepo extends JpaRepository<InvoiceTable, InvoiceTableId> {
//    // Tìm hóa đơn theo tableId
//    Optional<InvoiceTable> findByTableId(Integer tableId);
//
//    // Tìm invoiceId từ tableId
//    @Query("SELECT it.id.invoiceId FROM InvoiceTable it WHERE it.id.tableId = :tableId")
//    Optional<Integer> findInvoiceIdByTableId(@Param("tableId") Integer tableId);
//
//    Optional<InvoiceTable> findByInvoiceId(Integer invoiceId);
//
//    Optional<InvoiceTable> findByInvoiceIdAndTableId(Integer invoiceId, Integer tableId);
//
//
//}
