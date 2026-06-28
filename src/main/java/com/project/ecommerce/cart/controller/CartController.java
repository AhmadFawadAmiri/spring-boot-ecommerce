package com.project.ecommerce.user.controller;

import com.project.ecommerce.cart.entity.Cart;
import com.project.ecommerce.user.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId, @RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUser(userId));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId){
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
