package com.project.ecommerce.order.entity.repository;

import com.project.ecommerce.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
