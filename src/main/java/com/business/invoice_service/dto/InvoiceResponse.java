package com.business.invoice_service.dto;

public class InvoiceResponse {

    private Integer invoiceId;

    public InvoiceResponse(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
}
