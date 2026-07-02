package com.project.ecommerce.order.controller;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.dto.request.UpdateStatusRequest;
import com.project.ecommerce.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(){
        return ResponseEntity.ok(orderService.checkout());
    }

    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#orderId)")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(){
        return ResponseEntity.ok(orderService.getUserOrders());
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId,
                                      @RequestBody UpdateStatusRequest status){
        return ResponseEntity.ok(orderService.updateStatus(orderId, status.status()));
    }
}
