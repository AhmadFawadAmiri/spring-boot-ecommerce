package com.project.ecommerce.order.service;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse checkout(Long userId);
    OrderResponse updateStatus(Long orderId, OrderStatus newStatus);
    OrderResponse getById(Long id);
    List<OrderResponse> getUserOrders(Long userId);
    OrderResponse cancelOrder(Long orderId);
}
