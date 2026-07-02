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

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addToCart(productId, quantity));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(){
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long cartItemId){

        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> update(@PathVariable Long cartItemId,
                                                   @Valid @RequestBody CartItemRequest request){
        return ResponseEntity.ok(cartService.updateItem(cartItemId, request));
    }
}
