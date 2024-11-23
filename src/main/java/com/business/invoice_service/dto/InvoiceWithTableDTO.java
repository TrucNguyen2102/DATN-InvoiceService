package com.business.invoice_service.dto;

import com.business.invoice_service.entity.Invoice;

public class InvoiceWithTableDTO {
    private Invoice invoice;
    private Integer tableId;

    public InvoiceWithTableDTO(Invoice invoice, Integer tableId) {
        this.invoice = invoice;
        this.tableId = tableId;
    }


    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
}
