package com.project.ecommerce.order.controller;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.service.OrderService;
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

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrderResponse> checkout(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.checkout(userId));
    }
}
