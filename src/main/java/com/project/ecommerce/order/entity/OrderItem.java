package com.project.ecommerce.order.entity;

import com.project.ecommerce.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false)
    private int quantity;
    private BigDecimal priceAtPurchase;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
