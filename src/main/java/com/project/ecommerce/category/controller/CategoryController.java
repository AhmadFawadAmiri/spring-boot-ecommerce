package com.project.ecommerce.category.controller;

import com.project.ecommerce.category.dto.request.CategoryRequest;
import com.project.ecommerce.category.dto.response.CategoryResponse;
import com.project.ecommerce.category.entity.Category;
import com.project.ecommerce.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories", description = "Product management endpoints")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create all categories")
    //@ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }
    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(summary = "Get category by ID")

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @Operation(summary = "Update category by Admin")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request){
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @Operation(summary = "Delete category by Admin")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
