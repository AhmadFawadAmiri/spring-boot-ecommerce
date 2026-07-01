package com.project.ecommerce.category.repository;

import com.project.ecommerce.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    //Optional<Category> findByName(String name);
    List<Category> findByActiveTrue();
}
