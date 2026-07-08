package com.project.ecommerce.product.service;

import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.entity.ProductImage;
import com.project.ecommerce.product.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService{
    private final ProductImageRepository productImageRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileStorageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    public ProductImage store(MultipartFile file, Product product) {
        try{
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
            // create ProductImage here next
            ProductImage image = new ProductImage();
            image.setFileName(fileName);
            image.setFileUrl("/upload/products/" + fileName);
            image.setContentType(file.getContentType());
            image.setProduct(product);
            return productImageRepository.save(image);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file", ex);
        }
    }


    @Override
    public void delete(ProductImage image) {
        try{
            Path filePath = Paths.get(uploadDir).resolve(image.getFileName());
            Files.deleteIfExists(filePath);
            productImageRepository.delete(image);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete image",ex);
        }
    }
}
