package com.project.ecommerce.order.mapper;

import com.project.ecommerce.order.dto.response.OrderItemResponse;
import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderResponse toResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus().name());
        response.setCreateAt(order.getCreatedAt());
        List<OrderItemResponse> items = order.getOrderItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList();
        response.setItems(items);
        return response;
    }

    public OrderItemResponse toItemResponse(OrderItem item){
        OrderItemResponse orderItem = new OrderItemResponse();
        orderItem.setProductId(item.getProduct().getId());
        orderItem.setProductName(item.getProduct().getName());
        orderItem.setQuantity(item.getQuantity());
        orderItem.setPrice(item.getPriceAtPurchase());
        return orderItem;
    }
}
