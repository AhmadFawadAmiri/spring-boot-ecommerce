package com.project.ecommerce.notification.service;

import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.payment.entity.Payment;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.user.entity.User;

public interface EmailService {

    void sendPaymentSuccessEmail(Payment payment);
    void sendWelcomeEmail(User user);
    void sendOrderConfirmationEmail(Order order);
    void sendRefundEmail(Payment payment);
    void sendWelcomeNewUserEmail(User user);
    void emailLowStock(Product product);
}
