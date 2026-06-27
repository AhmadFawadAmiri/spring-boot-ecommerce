package com.project.ecommerce.user.repository;

import com.project.ecommerce.user.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
