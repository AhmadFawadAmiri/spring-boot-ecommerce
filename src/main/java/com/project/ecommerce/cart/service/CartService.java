package com.project.ecommerce.cart.service;

import com.project.ecommerce.cart.dto.request.CartItemRequest;
import com.project.ecommerce.cart.dto.response.CartItemResponse;
import com.project.ecommerce.cart.dto.response.CartResponse;



public interface CartService {
    CartResponse addToCart(Long productId, int quantity);
    CartResponse getCart();
    void removeItem(Long cartItemId);
    CartItemResponse updateItem(Long cartItemId, CartItemRequest item);
}
