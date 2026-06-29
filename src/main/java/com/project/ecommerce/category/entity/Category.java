package com.project.ecommerce.category.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.ecommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    private String description;
    @OneToMany(mappedBy = "category")
    //@JsonManagedReference
    private List<Product> products;
}
