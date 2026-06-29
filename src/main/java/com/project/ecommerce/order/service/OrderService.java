package com.project.ecommerce.order.service;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.entity.OrderStatus;

public interface OrderService {
    OrderResponse checkout(Long userId);
    OrderResponse updateStatus(Long orderId, OrderStatus newStatus);
}
