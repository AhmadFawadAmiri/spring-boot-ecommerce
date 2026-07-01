package com.project.ecommerce.category.service;

import com.project.ecommerce.category.dto.request.CategoryRequest;
import com.project.ecommerce.category.dto.response.CategoryResponse;
import com.project.ecommerce.category.entity.Category;
import com.project.ecommerce.category.mapper.CategoryMapper;
import com.project.ecommerce.category.repository.CategoryRepository;
import com.project.ecommerce.global.exception.DuplicateResourceException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if(categoryRepository.existsByName(request.getName())){
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findByActiveTrue().stream()
                .map(categoryMapper::toResponse).toList();
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = getCategoryById(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request){
        Category category = getCategoryById(id);
        String name = request.getName().trim();
        if(!category.getName().equals(request.getName())
                && categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("Category already exists");
        }
        category.setName(name);
        category.setDescription(request.getDescription());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        Category category = getCategoryById(id);
        if(!category.getProducts().isEmpty()){
            throw new RuntimeException("Cannot delete category with products");
        }
        category.setActive(false);
        categoryRepository.save(category);
    }

    private Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not found"));
    }
}
