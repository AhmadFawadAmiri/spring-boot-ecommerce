package com.project.ecommerce.notification.service;

import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.payment.entity.Payment;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    @Value("${inventory.alert.email}")
    private String inventoryAlertEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPaymentSuccessEmail(Payment payment) {
        String subject = "Payment Successful";
        String body = """
                Hello %s,
                
                Your payment has been processed successfully.
                
                Order ID: %s
                Amount: %s
                Payment Method: %s
                
                Thank you for shopping with us!
                
                E-Commerce Team
                
                """.formatted(payment.getOrder().getUser().getUsername(),
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod());
        sendEmail(payment.getOrder().getUser().getEmail(), subject, body);
    }

    @Override
    public void sendWelcomeEmail(User user) {
        String subject = "Welcome! ";
        String body = """
                Hello %s,
                
                Welcome to your ecommerce shop!
                
                """.formatted(user.getUsername());
        sendEmail(user.getEmail(), subject, body);
    }

    @Override
    public void sendOrderConfirmationEmail(Order order) {
        String subject = "Order Successful";
        String body = """
                Hello %s,
                
                Your order has been checked successfully.
                
                Order ID: %s
                Amount: %s
                
                Thank you for shopping with us!
                
                E-Commerce Team
                
                """.formatted(order.getUser().getUsername(),
                order.getId(),
                order.getTotalPrice());
        sendEmail(order.getUser().getEmail(), subject, body);

    }

    @Override
    public void sendRefundEmail(Payment payment) {
        String subject = "Payment Refunded";
        String body = """
                Hello %s,
                
                Your payment has been refunded successfully.
                
                Order ID: %s
                Amount: %s
                Payment Method: %s
                
                Thank you for shopping with us!
                
                E-Commerce Team
                
                """.formatted(payment.getOrder().getUser().getUsername(),
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod());
        sendEmail(payment.getOrder().getUser().getEmail(), subject, body);
    }

    @Override
    public void sendWelcomeNewUserEmail(User user) {
        String subject = "Welcome! ";
        String body = """
                Hello %s,
                
                Welcome to your ecommerce shop!
                
                Here you can order anything you want, this your shop.
                
                """.formatted(user.getUsername());
        sendEmail(user.getEmail(), subject, body);
    }

    public void emailLowStock(Product product){
        String subject = "Low Stock Alert";
        String body = """
                Product: %s
                Current stock: %d
                """.formatted(product.getName() ,product.getStockQuantity());
        sendEmail(inventoryAlertEmail, subject, body);
    }

    // private methods
    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        /// /
        System.out.println("SENDING EMAIL:");
        System.out.println("FROM (SMTP USER): " + mailSender);
        System.out.println("TO: " + to);
        System.out.println("SUBJECT: " + subject);
        System.out.println("EMAIL ENV = " + System.getenv("EMAIL"));
        System.out.println("APP PASSWORD SET = " + (System.getenv("APP_PASSWORD") != null));
    }


}
