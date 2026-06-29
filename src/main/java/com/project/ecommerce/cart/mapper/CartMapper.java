package com.project.ecommerce.cart.mapper;

import com.project.ecommerce.cart.dto.request.CartItemRequest;
import com.project.ecommerce.cart.dto.response.CartItemResponse;
import com.project.ecommerce.cart.dto.response.CartResponse;
import com.project.ecommerce.cart.entity.Cart;
import com.project.ecommerce.cart.entity.CartItem;
import com.project.ecommerce.product.mapper.ProductMapper;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    private final ProductMapper productMapper;

    public CartMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public CartResponse toResponse(Cart cart){
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setName(cart.getUser().getUsername());
        response.setCartItems(cart.getCartItems()
                .stream().map(this::toCartItemResponse).toList());
        return response;
    }

    public CartItemResponse toCartItemResponse(CartItem item){
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setProduct(productMapper.toResponse(item.getProduct()));
        return response;
    }

    public void updateCartItem(CartItem existing, CartItemRequest request, Cart cart){
        existing.setQuantity(request.getQuantity());
        existing.setCart(cart);
    }
}
