package com.example.demo.validation;

import com.example.demo.dto.CategoryRequest;

public class CategoryValidation {

    public static String validateCategoryRequest(CategoryRequest request) {
        if (request == null) {
            return "Category request cannot be null";
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Category name is required";
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            return "Description is required";
        }

        return null; // All validations passed
    }
}
