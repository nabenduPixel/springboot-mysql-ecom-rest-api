package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CategoryRequest;
import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class CategoryService {

    private static final String BUCKET_NAME = "blog-4c826.appspot.com"; // Use your correct Firebase Storage bucket name

    @Autowired
    private FileStorageService imageUploadService; // Class where `uploadImage()` lives

    @Autowired
    private CategoryRepository categoryRepository;

    // Example method to get all categories (to be implemented)
    public List<Category> getAllCategories() {
        return categoryRepository.findByStatus(Category.Status.ACTIVE);
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category saveCategory(CategoryRequest category) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());

        if (category.getParentId() != null) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            newCategory.setParent(parent);
        } else {
            newCategory.setParent(null);
        }

        // Upload image and set URL
        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
            try {
                String imageUrl = imageUploadService.uploadImage(category.getImageUrl());
                newCategory.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }

        return categoryRepository.save(newCategory);
    }

    public Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Boolean findCategoryy(Long id) {
        return categoryRepository.existsById(id);
    }

    // public Category updateCategory(Category category) {
    // return categoryRepository.save(category);
    // }

    public Category updateCategory(Category category, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete previous image if exists
            if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
                deletePreviousImage(category.getImageUrl());
            }

            // Upload new image
            String imageUrl = imageUploadService.uploadImage(imageFile);
            category.setImageUrl(imageUrl);
        }

        return categoryRepository.save(category);
    }

    private void deletePreviousImage(String imageUrl) {
        try {
            StorageClient storageClient = StorageClient.getInstance();
            Bucket bucket = storageClient.bucket(BUCKET_NAME);

            if (imageUrl != null && imageUrl.contains(BUCKET_NAME)) {
                String fileName = imageUrl.substring(imageUrl.indexOf("uploads/category/"));

                // Delete the blob
                Blob blob = bucket.get(fileName);
                if (blob != null) {
                    blob.delete();
                }
            }
        } catch (Exception e) {
            // Log the error but don't throw exception to avoid breaking the update process
            System.err.println("Failed to delete previous image: " + e.getMessage());
        }
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public boolean deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
