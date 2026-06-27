package com.project.ecommerce.product.service;

import com.project.ecommerce.product.dto.ProductRequest;
import com.project.ecommerce.product.dto.ProductResponse;
import com.project.ecommerce.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductResponse createProduct(ProductRequest Request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest product);

    void deleteProduct(Long id);

}
