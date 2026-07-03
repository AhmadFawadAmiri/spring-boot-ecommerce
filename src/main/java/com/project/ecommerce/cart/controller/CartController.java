package com.project.ecommerce.cart.controller;

import com.project.ecommerce.cart.dto.request.CartItemRequest;
import com.project.ecommerce.cart.dto.response.CartItemResponse;
import com.project.ecommerce.cart.dto.response.CartResponse;
import com.project.ecommerce.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "Shopping cart endpoints")
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Add product to carts")
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@RequestParam Long productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addToCart(productId, quantity));
    }

    @GetMapping    @Operation(summary = "Get current user's cart")
    public ResponseEntity<CartResponse> getCart(){
        return ResponseEntity.ok(cartService.getCart());
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long cartItemId){

        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update cart item quantity")
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> update(@PathVariable Long cartItemId,
                                                   @Valid @RequestBody CartItemRequest request){
        return ResponseEntity.ok(cartService.updateItem(cartItemId, request));
    }
}
