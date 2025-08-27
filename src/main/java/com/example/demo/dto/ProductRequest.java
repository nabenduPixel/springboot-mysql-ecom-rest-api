package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private String description;
    private Long categoryId; // Optional, for subcategories
    private MultipartFile imageUrl;
    private Double price;
    private Integer quantity;
    private String status; // e.g., "active", "inactive"
}
