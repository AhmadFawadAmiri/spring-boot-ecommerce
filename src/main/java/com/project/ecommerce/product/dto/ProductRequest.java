package com.project.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    @NotBlank
    @Size(max = 100, message = "Name must not be blank")
    private String name;
    @Size(max = 1000, message = "Description too long")
    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    @Min(value = 0, message = "Stock cannot be negative")
    private int stockQuantity;
    @NotNull(message = "Category is required")
    private Long categoryId;
}
