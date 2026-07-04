package com.project.ecommerce.payment.controller;

import com.project.ecommerce.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
public class PaymentWebhookController {
    private final PaymentService paymentService;

    public PaymentWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/payments/{paymentId}")
    public ResponseEntity<Void> paymentWebhook(@PathVariable Long paymentId,
                                               @RequestParam boolean success){
        paymentService.processWebhook(paymentId, success);
        return ResponseEntity.ok().build();
    }
}
