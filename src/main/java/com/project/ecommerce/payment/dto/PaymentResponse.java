package com.project.ecommerce.payment.dto;

import com.project.ecommerce.payment.entity.PaymentMethod;
import com.project.ecommerce.payment.entity.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse( Long paymentId,
                               Long orderId,
                               BigDecimal amount,
                               PaymentStatus status,
                               PaymentMethod method
) {}
