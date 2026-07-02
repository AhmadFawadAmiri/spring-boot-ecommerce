package com.project.ecommerce.order.security;

import com.project.ecommerce.order.repository.OrderRepository;
import com.project.ecommerce.user.entity.User;
import com.project.ecommerce.user.repository.UserRepository;
import com.project.ecommerce.user.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component("orderSecurity")
public class OrderSecurity {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderSecurity(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long orderId){
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        return orderRepository.existsByIdAndUserId(orderId, user.getId());
    }
}
