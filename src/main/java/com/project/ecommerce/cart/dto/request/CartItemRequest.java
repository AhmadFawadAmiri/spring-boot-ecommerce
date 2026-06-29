package com.project.ecommerce.cart.dto.request;

import com.project.ecommerce.product.entity.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    @NotNull
    @Min(1)
    private Long productId;
    @Positive
    private int quantity;
}
