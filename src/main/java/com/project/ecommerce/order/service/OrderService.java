package com.project.ecommerce.order.service;

import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.Order;

public interface OrderService {
    OrderResponse checkout(Long userId);
}
