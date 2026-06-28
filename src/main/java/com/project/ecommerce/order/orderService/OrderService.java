package com.project.ecommerce.order.orderService;

import com.project.ecommerce.order.entity.Order;

public interface OrderService {
    Order createOrderFromCart(Long userId);
}
