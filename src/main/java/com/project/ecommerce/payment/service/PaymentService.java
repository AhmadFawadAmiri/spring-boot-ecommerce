package com.project.ecommerce.payment.service;

import com.project.ecommerce.payment.dto.PaymentResponse;
import com.project.ecommerce.payment.entity.Payment;
import com.project.ecommerce.payment.entity.PaymentMethod;

import java.util.List;

public interface PaymentService {
    PaymentResponse pay(Long orderId, PaymentMethod method);
    PaymentResponse getByOrderId(Long orderId);
    void processWebhook(Long paymentId, boolean success);
    List<PaymentResponse> getMyPayments();
    PaymentResponse refund(Long paymentId);

}
