package com.project.ecommerce.user.service;

import com.project.ecommerce.user.entity.Cart;


public interface CartService {
    Cart addToCart(Long userId, Long productId, int quantity);
    Cart getCartByUser(Long userId);
    void removeItem(Long cartItemId);
}
