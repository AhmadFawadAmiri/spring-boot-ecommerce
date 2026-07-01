package com.project.ecommerce.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class addCartItemRequest {
    @NotNull
    private Long productId;
    @Positive
    private int quantity;
}
