package com.project.ecommerce.order.dto.request;

import com.project.ecommerce.order.entity.OrderStatus;

public record UpdateStatusRequest(OrderStatus status) {
}
