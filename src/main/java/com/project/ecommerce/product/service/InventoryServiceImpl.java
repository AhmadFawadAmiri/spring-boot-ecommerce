package com.project.ecommerce.product.service;

import com.project.ecommerce.notification.service.EmailService;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService{
    private final ProductRepository productRepository;
    private final EmailService emailService;
    @Value("${inventory.low-stock-threshold}")
    private int threshold;

    public InventoryServiceImpl(ProductRepository productRepository, EmailService emailService) {
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    @Override
    public void checkLowStock(Product product) {
        if(product.getStockQuantity() <= threshold){
            emailService.emailLowStock(product);
        }
    }


}
