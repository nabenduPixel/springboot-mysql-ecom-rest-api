package com.example.demo.validation;

import com.example.demo.dto.ProductRequest;

public class ProductValidation {

     public static String validateProductRequest(ProductRequest request) {
        if (request == null) {
            return "Product request cannot be null";
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Product name is required";
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            return "Description is required";
        }

        if (request.getPrice() == null || request.getPrice() <= 0) {
            return "Price must be greater than zero";
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            return "Quantity must be greater than zero";
        }

        if(request.getCategoryId() == null) {
            return "Category id is required";
        }

        if(request.getImageUrl() == null) {
            return "Image is required";
        }

        if(request.getStatus() == null) {
            return "Status is required";
        }

        return null; // All validations passed
    }

}
