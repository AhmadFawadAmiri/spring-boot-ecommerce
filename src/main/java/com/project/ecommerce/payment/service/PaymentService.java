package com.project.ecommerce.payment.service;

import com.project.ecommerce.payment.dto.PaymentResponse;
import com.project.ecommerce.payment.entity.PaymentMethod;

public interface PaymentService {
    PaymentResponse pay(Long orderId, PaymentMethod method);
    PaymentResponse getByOrderId(Long orderId);
}
