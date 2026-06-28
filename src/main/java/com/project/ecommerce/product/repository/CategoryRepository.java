package com.project.ecommerce.product.repository;

import com.project.ecommerce.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
