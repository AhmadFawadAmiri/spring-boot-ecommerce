package com.project.ecommerce.payment.controller;

import com.project.ecommerce.payment.dto.PaymentResponse;
import com.project.ecommerce.payment.entity.PaymentMethod;
import com.project.ecommerce.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> pay(@PathVariable Long orderId,
                                               @RequestParam PaymentMethod method){
        return ResponseEntity.ok(paymentService.pay(orderId, method));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(paymentService.getByOrderId(orderId));
    }
}
