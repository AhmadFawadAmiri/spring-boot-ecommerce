package com.project.ecommerce.payment.mapper;

import com.project.ecommerce.payment.dto.PaymentResponse;
import com.project.ecommerce.payment.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponse toResponse(Payment payment){
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod()
        );
    }
}
