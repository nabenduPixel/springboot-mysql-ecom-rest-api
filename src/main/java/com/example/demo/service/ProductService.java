package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

    private static final String BUCKET_NAME = "blog-4c826.appspot.com"; // Use your correct Firebase Storage bucket name

    @Autowired
    private FileStorageService imageUploadService; // Class where `uploadImage()` lives

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByStatus(Product.Status.ACTIVE);
    }

    public Product saveProduct(ProductRequest product) {
        Product newProduct = new Product();
        newProduct.setName(product.getName());
        newProduct.setDescription(product.getDescription());
        newProduct.setPrice(product.getPrice());
        newProduct.setQuantity(product.getQuantity());
        // newProduct.setCategoryId(product.getCategoryId());
        newProduct.setStatus(Product.Status.ACTIVE);

        // Upload image and set URL
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                String imageUrl = imageUploadService.uploadImage(product.getImageUrl());
                newProduct.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }

        return productRepository.save(newProduct);
    }

}
