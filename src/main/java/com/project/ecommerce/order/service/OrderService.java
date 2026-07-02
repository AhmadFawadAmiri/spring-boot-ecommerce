package com.project.ecommerce.order.service;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse checkout();
    OrderResponse updateStatus(Long orderId, OrderStatus newStatus);
    OrderResponse getById(Long id);
    List<OrderResponse> getUserOrders();
    OrderResponse cancelOrder(Long orderId);
}
