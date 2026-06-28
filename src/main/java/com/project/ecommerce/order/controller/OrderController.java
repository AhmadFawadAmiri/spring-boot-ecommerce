package com.project.ecommerce.order.controller;

import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.orderService.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Order> checkout(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.createOrderFromCart(userId));
    }
}
