package com.project.ecommerce.product.repository;

import com.project.ecommerce.product.entity.Category;
import com.project.ecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
