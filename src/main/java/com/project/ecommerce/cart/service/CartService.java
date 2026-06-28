package com.project.ecommerce.cart.service;

import com.project.ecommerce.cart.entity.Cart;


public interface CartService {
    Cart addToCart(Long userId, Long productId, int quantity);
    Cart getCartByUser(Long userId);
    void removeItem(Long cartItemId);
}
