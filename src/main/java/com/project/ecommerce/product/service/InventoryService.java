package com.project.ecommerce.product.service;

import com.project.ecommerce.product.entity.Product;

public interface InventoryService {
    void checkLowStock(Product product);
}
