package com.project.ecommerce.payment.controller;

import com.project.ecommerce.payment.dto.PaymentResponse;
import com.project.ecommerce.payment.entity.PaymentMethod;
import com.project.ecommerce.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment", description = "Payment management endpoints")
@RestController
@RequestMapping("api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create all payments")
    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> pay(@PathVariable Long orderId,
                                               @RequestParam PaymentMethod method){
        return ResponseEntity.ok(paymentService.pay(orderId, method));
    }

    @Operation(summary = "Get payment")
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(paymentService.getByOrderId(orderId));
    }
}
