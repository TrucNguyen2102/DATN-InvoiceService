package com.business.invoice_service.controller;

import com.business.invoice_service.entity.PaymentMethod;
import com.business.invoice_service.repository.PaymentMethodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class MethodController {

    @Autowired
    private PaymentMethodRepo paymentMethodRepo;

    @GetMapping("/payments/all")
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepo.findAll();
    }
}
