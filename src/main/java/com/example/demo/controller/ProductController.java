package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.example.demo.utils.ResponseHandeler;
import com.example.demo.validation.ProductValidation;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService ProductService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = ProductService.getAllProducts();
            return ResponseHandeler.generateResponse(HttpStatus.OK, "Product list retrieved successfully.", products);
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve product list.", null);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductRequest request) {
        try {
            String validationError = ProductValidation.validateProductRequest(request);

            if (validationError != null) {
                return ResponseHandeler.generateResponse(
                        HttpStatus.BAD_REQUEST,
                        validationError,
                        null);
            }
            ProductService.saveProduct(request);
            return ResponseHandeler.generateResponse(HttpStatus.CREATED, "Product saved successfully.", null);
        } catch (Exception e) {
             return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save product.", null);
        }
    }

}
