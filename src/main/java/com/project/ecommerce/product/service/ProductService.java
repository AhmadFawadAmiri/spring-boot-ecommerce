package com.project.ecommerce.product.service;

import com.project.ecommerce.product.dto.request.ProductRequest;
import com.project.ecommerce.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface ProductService {

    ProductResponse createProduct(ProductRequest Request);

    Page<ProductResponse> getAllActiveProducts(Pageable pageable);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    Page<ProductResponse> search(String name, Pageable pageable);

    //Page<ProductResponse> filter(String type, String value, Pageable pageable);

    Page<ProductResponse> filter(String name, Long categoryId, BigDecimal minPrice, Pageable pageable);

    void uploadImage(Long productId, MultipartFile file);
}
