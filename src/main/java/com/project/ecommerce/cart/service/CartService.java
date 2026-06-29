package com.project.ecommerce.cart.service;

import com.project.ecommerce.cart.dto.request.CartItemRequest;
import com.project.ecommerce.cart.dto.response.CartItemResponse;
import com.project.ecommerce.cart.dto.response.CartResponse;


public interface CartService {
    CartResponse addToCart(Long userId, Long productId, int quantity);
    CartResponse getCartByUser(Long userId);
    void removeItem(Long userId, Long cartItemId);
    CartItemResponse updateItem(Long id, CartItemRequest item);
}
