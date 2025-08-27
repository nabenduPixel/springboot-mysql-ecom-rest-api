package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CategoryRequest;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.utils.ResponseHandeler;
import com.example.demo.validation.CategoryValidation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<?> listCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseHandeler.generateResponse(HttpStatus.OK, "Category list retrieved successfully.",
                    categories);
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve category list.", null);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@ModelAttribute CategoryRequest request) {
        try {
            String validationError = CategoryValidation.validateCategoryRequest(request);
            if (validationError != null) {
                return ResponseHandeler.generateResponse(
                        HttpStatus.BAD_REQUEST,
                        validationError,
                        null);
            }

            if (categoryService.findByName(request.getName()).isPresent()) {
                return ResponseHandeler.generateResponse(
                        HttpStatus.BAD_REQUEST,
                        "Category name is already in use",
                        null);
            }

            Category data = categoryService.saveCategory(request);

            return ResponseHandeler.generateResponse(HttpStatus.CREATED, "Category saved successfully.", data);
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save category.",
                    e.getMessage());
        }
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<?> findCategory(@PathVariable Long id) {
        try {
            Category category = categoryService.findCategory(id);
            return ResponseHandeler.generateResponse(HttpStatus.OK, "Category find retrieved successfully.", category);
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @RequestParam(required = false) MultipartFile image,
            @RequestBody Category request) {
        try {
            Category existingCategory = categoryService.findById(id);
            if (existingCategory == null) {
                return ResponseHandeler.generateResponse(HttpStatus.NOT_FOUND, "Category not found.", null);
            }

            // Merge fields from request into existing category
            if (request.getName() != null) {
                existingCategory.setName(request.getName());
            }
            if (request.getDescription() != null) {
                existingCategory.setDescription(request.getDescription());
            }
            if (request.getParent() != null) {
                existingCategory.setParent(request.getParent());
            }

            // Update category with image handling
            Category updatedCategory = categoryService.updateCategory(existingCategory, image);
            return ResponseHandeler.generateResponse(HttpStatus.OK, "Category updated successfully.", updatedCategory);

        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while updating the category.", null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            boolean deleted = categoryService.deleteCategory(id);
            if (deleted) {
                return ResponseHandeler.generateResponse(HttpStatus.OK, "Category deleted successfully.", null);
            } else {
                return ResponseHandeler.generateResponse(HttpStatus.NOT_FOUND, "Category not found.", null);
            }
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while deleting the category.", null);
        }
    }

}
