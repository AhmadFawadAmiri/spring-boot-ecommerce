package com.project.ecommerce.order.controller;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.dto.request.UpdateStatusRequest;
import com.project.ecommerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "Order management endpoints")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create all checkouts")
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(){
        return ResponseEntity.ok(orderService.checkout());
    }

    @Operation(summary = "Get order by ID")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#orderId)")
    @GetMapping("/oder/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @Operation(summary = "Get all orders")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(){
        return ResponseEntity.ok(orderService.getUserOrders());
    }

    @Operation(summary = "Cancel order")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#orderId)")
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @Operation(summary = "Update order")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId,
                                      @RequestBody UpdateStatusRequest status){
        return ResponseEntity.ok(orderService.updateStatus(orderId, status.status()));
    }
}
