package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private Long id;
    private String title;
    private String FullName;
    private String email;
    private String mobile_code;
    private String mobile_number;
    private String gender;
    private String token;
    private Long roleId;
    private String roleName;
    private String status;
}
