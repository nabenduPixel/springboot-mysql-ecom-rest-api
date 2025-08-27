package com.example.demo.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    private String title;

    private String first_name;

    private String last_name;

    private String email;

    private String password;

    private String mobile_code;

    private String mobile_number;

    private String gender;

    private String role_id; // e.g., "user", "admin", etc.

}
