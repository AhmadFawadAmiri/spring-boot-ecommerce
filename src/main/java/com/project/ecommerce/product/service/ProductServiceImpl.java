package com.project.ecommerce.product.service;

import com.project.ecommerce.product.dto.ProductRequest;
import com.project.ecommerce.product.dto.ProductResponse;
import com.project.ecommerce.product.entity.Category;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.repository.CategoryRepository;
import com.project.ecommerce.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()->new EntityNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);

        return mapToResponse(productRepository.save(product));
    }

    private ProductResponse mapToResponse(Product product){

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCreatedAt(product.getCreatedAt());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());

        return response;
    }
    private List<ProductResponse> mapToResponse(List<Product> products){

        List<ProductResponse> responses = new ArrayList<>();
        for(Product product : products){
            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setStockQuantity(product.getStockQuantity());
            response.setCreatedAt(product.getCreatedAt());
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> existing = productRepository.findAll();
        return mapToResponse(existing);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Product not found"));
        return mapToResponse(existing);
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(()->new EntityNotFoundException("Category not found"));

        Product existing = productRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Product not found"));
        existing.setName(productRequest.getName());
        existing.setDescription(productRequest.getDescription());
        existing.setCategory(category);
        existing.setPrice(productRequest.getPrice());
        existing.setStockQuantity(productRequest.getStockQuantity());

        return mapToResponse(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)){
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
