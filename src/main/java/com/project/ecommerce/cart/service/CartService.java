package com.project.ecommerce.cart.service;

import com.project.ecommerce.cart.dto.request.CartItemRequest;
import com.project.ecommerce.cart.dto.response.CartItemResponse;
import com.project.ecommerce.cart.dto.response.CartResponse;

import java.nio.file.AccessDeniedException;


public interface CartService {
    CartResponse addToCart(Long userId, Long productId, int quantity);
    CartResponse getCartByUser(Long userId);
    void removeItem(Long userId, Long cartItemId) throws AccessDeniedException;
    CartItemResponse updateItem(Long id, CartItemRequest item);
}
