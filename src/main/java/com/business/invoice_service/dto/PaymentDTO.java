package com.business.invoice_service.dto;

public class PaymentDTO {
    private String paymentMethod;
    private double totalAmount;

    public PaymentDTO(String paymentMethod, double totalAmount) {
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
