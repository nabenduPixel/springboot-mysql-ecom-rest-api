package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;
    private String description;
    private Long parentId; // Optional, for subcategories
    private MultipartFile imageUrl;
    private String status; // e.g., "active", "inactive"


}
