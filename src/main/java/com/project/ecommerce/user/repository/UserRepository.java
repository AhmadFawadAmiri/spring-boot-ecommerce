package com.project.ecommerce.user.repository;

import com.project.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
