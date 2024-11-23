//package com.business.invoice_service.entity;
//
//import jakarta.persistence.Column;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class InvoiceTableId implements Serializable {
//    @Column(name = "invoice_id")
//    private Integer invoiceId;
//    @Column(name = "table_id")
//    private Integer tableId;
//
//    public InvoiceTableId() {
//
//    }
//
//    public InvoiceTableId(Integer invoiceId, Integer tableId) {
//        this.invoiceId = invoiceId;
//        this.tableId = tableId;
//    }
//
//    public Integer getInvoiceId() {
//        return invoiceId;
//    }
//
//    public void setInvoiceId(Integer invoiceId) {
//        this.invoiceId = invoiceId;
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
//    // Implement equals và hashCode
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        InvoiceTableId that = (InvoiceTableId) o;
//        return Objects.equals(invoiceId, that.invoiceId) && Objects.equals(tableId, that.tableId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(invoiceId, tableId);
//    }
//}
