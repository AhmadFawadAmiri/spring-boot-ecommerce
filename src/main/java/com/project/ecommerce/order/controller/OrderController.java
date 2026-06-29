package com.project.ecommerce.order.controller;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.OrderStatus;
import com.project.ecommerce.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId,
                                      @RequestParam OrderStatus status){
        return ResponseEntity.ok(orderService.updateStatus(orderId, status));
    }
}
