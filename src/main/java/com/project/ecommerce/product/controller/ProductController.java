package com.project.ecommerce.product.controller;

import com.project.ecommerce.product.dto.request.ProductRequest;
import com.project.ecommerce.product.dto.response.ProductResponse;
import com.project.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(@PageableDefault(size = 5, sort = "id") Pageable pageable){
        return ResponseEntity.ok(productService.getAllActiveProducts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest product){
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> search(@RequestParam String name, Pageable pageable){
        return ResponseEntity.ok(productService.search(name, pageable));
    }
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponse>> filter(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String value,
            Pageable pageable){
        return ResponseEntity.ok(productService.filter(type, value, pageable));
    }
}
