package com.project.ecommerce.product.service;

import com.project.ecommerce.product.dto.request.ProductRequest;
import com.project.ecommerce.product.dto.response.ProductResponse;
import com.project.ecommerce.category.entity.Category;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.mapper.ProductMapper;
import com.project.ecommerce.category.repository.CategoryRepository;
import com.project.ecommerce.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(()->new EntityNotFoundException("Category not found"));
        Product product = productMapper.toEntity(request, category);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(()->new EntityNotFoundException("Product not found"));
        return productMapper.toResponse(product);
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Category category = categoryRepository
                .findById(productRequest.getCategoryId())
                .orElseThrow(()->new EntityNotFoundException("Category not found"));
        Product product = productRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Product not found"));
        productMapper.updateEntity(product, productRequest, category);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(("Product not found")));
        productRepository.delete(product);
    }
}
