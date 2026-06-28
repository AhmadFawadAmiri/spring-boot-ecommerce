package com.project.ecommerce.product.service;

import com.project.ecommerce.product.dto.request.ProductRequest;
import com.project.ecommerce.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest Request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

}
