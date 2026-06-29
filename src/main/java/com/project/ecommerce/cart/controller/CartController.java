package com.project.ecommerce.cart.controller;

import com.project.ecommerce.cart.dto.request.CartItemRequest;
import com.project.ecommerce.cart.dto.response.CartItemResponse;
import com.project.ecommerce.cart.dto.response.CartResponse;
import com.project.ecommerce.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long userId, @RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUser(userId));
    }

    @DeleteMapping("/item/{userId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long userId, @RequestParam() Long itemId){

        cartService.removeItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/items/{id}")
    public ResponseEntity<CartItemResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CartItemRequest request){
        return ResponseEntity.ok(cartService.updateItem(id, request));
    }
}
