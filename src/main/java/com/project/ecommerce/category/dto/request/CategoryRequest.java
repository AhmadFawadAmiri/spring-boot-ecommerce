package com.project.ecommerce.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank
    private String name;
}
