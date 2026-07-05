package com.project.ecommerce.payment.controller;

import com.project.ecommerce.payment.dto.PaymentResponse;
import com.project.ecommerce.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Webhook", description = "Webhook for payment")
@RestController
@RequestMapping("/api/webhook")
public class PaymentWebhookController {
    private final PaymentService paymentService;

    public PaymentWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Webhook of payment")
    @PostMapping("/payments/{paymentId}")
    public ResponseEntity<String> paymentWebhook(@PathVariable Long paymentId,
                                               @RequestParam boolean success){
        paymentService.processWebhook(paymentId, success);
        return ResponseEntity.ok().body("Webhook");
    }
}
