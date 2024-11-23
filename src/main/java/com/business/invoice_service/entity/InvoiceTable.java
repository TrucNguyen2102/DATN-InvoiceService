//package com.business.invoice_service.entity;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//
//import java.io.Serializable;
//@Entity
//@Table(name = "invoice_table")
//public class InvoiceTable implements Serializable {
//    @EmbeddedId
//    private InvoiceTableId id;
//    @ManyToOne
//    @MapsId("invoiceId")
//    @JoinColumn(name = "invoice_id")
//    @JsonBackReference
//    private Invoice invoice;
//
//    @Column(name = "table_id", insertable=false, updatable=false)
//    private Integer tableId; // Khóa ngoại của table từ table-service
//
//    public InvoiceTableId getId() {
//        return id;
//    }
//
//    public void setId(InvoiceTableId id) {
//        this.id = id;
//    }
//
//    public Invoice getInvoice() {
//        return invoice;
//    }
//
//    public void setInvoice(Invoice invoice) {
//        this.invoice = invoice;
//    }
//
//    public Integer getTableId() {
//        return tableId;
//    }
//
//    public void setTableId(Integer tableId) {
//        this.tableId = tableId;
//    }
//
//
//}
