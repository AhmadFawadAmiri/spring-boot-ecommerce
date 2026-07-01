package com.project.ecommerce.product.service;

import com.project.ecommerce.product.dto.request.ProductRequest;
import com.project.ecommerce.product.dto.response.ProductResponse;
import com.project.ecommerce.category.entity.Category;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.mapper.ProductMapper;
import com.project.ecommerce.category.repository.CategoryRepository;
import com.project.ecommerce.product.repository.ProductRepository;
import com.project.ecommerce.product.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.function.LongToDoubleFunction;

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
    public Page<ProductResponse> getAllActiveProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByActiveTrue(pageable);
        return products.map(productMapper::toResponse);
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
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public Page<ProductResponse> search(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(productMapper::toResponse);
    }

//    @Override
//    public Page<ProductResponse> filter(String type, String value, Pageable pageable) {
//        if(type == null || !StringUtils.hasText(value)){
//            return getAllActiveProducts(pageable);
//        }
//        return switch (type){
//            case "category" -> {
//                Long categoryId;
//                try{
//                    categoryId = Long.parseLong(value);
//                } catch (NumberFormatException e) {
//                    throw new IllegalArgumentException("Invalid category id");
//                }
//                yield productRepository.findByCategory_Id(categoryId, pageable)
//                        .map(productMapper::toResponse);
//            }
//            case "name" -> search(value, pageable);
//            default -> getAllActiveProducts(pageable);
//        };
//    }
//    ///////////

    @Override
    public Page<ProductResponse> filter(String name, Long categoryId, BigDecimal minPrice, Pageable pageable){
        Specification<Product> spec = Specification.where(ProductSpecification.isActive());
        if(categoryId != null){
            spec = spec.and(ProductSpecification.hasCategory(categoryId));
        }
        if(StringUtils.hasText(name)){
            spec = spec.and(ProductSpecification.nameContains(name));
        }
        if(minPrice != null){
            spec = spec.and(ProductSpecification.priceGreaterThan(minPrice));
        }
        return productRepository.findAll(spec, pageable)
                .map(productMapper::toResponse);

    }




}
