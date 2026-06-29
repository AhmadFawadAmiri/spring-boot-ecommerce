package com.project.ecommerce.category.service;

import com.project.ecommerce.category.dto.request.CategoryRequest;
import com.project.ecommerce.category.dto.response.CategoryResponse;
import com.project.ecommerce.category.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    List<CategoryResponse> getAll();
    CategoryResponse getById(Long id);
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
}
