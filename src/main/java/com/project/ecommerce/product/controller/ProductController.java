package com.project.ecommerce.product.controller;

import com.project.ecommerce.product.dto.request.ProductRequest;
import com.project.ecommerce.product.dto.response.ProductResponse;
import com.project.ecommerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products", description = "Product management endpoints")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create all products")
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable){
        return ResponseEntity.ok(productService.getAllActiveProducts(pageable));
    }

    @Operation(summary = "Get product by Admin")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@Parameter(description = "Product ID") @PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Update product by Admin")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest product){
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @Operation(summary = "Delete product by Admin")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search by product")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> search(@RequestParam String name, @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable){
        return ResponseEntity.ok(productService.search(name, pageable));
    }

    @Operation(summary = "filter by product and price")
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponse>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable){
        return ResponseEntity.ok(productService.filter(name, categoryId, minPrice, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/images")
    public ResponseEntity<Void> uploadImage(@PathVariable Long productId, @RequestParam("file")MultipartFile file){
        productService.uploadImage(productId, file);
        return ResponseEntity.ok().build();
    }
}
