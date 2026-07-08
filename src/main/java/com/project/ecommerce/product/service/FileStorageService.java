package com.project.ecommerce.product.service;

import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    // save()
    ProductImage store(MultipartFile file, Product product);
    // delete()
    void delete(ProductImage image);
    // load()
}
