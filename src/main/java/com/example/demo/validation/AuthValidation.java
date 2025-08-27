package com.example.demo.validation;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserRegistrationRequest;

public class AuthValidation {

    public static String validateUserRegistration(UserRegistrationRequest request) {

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            return "Title is required";
        }

        if (request.getFirst_name() == null || request.getFirst_name().trim().isEmpty()) {
            return "First name is required";
        }

        if (request.getLast_name() == null || request.getLast_name().trim().isEmpty()) {
            return "Last name is required";
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return "Email is required";
        }

        if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "Email format is invalid";
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return "Password too short";
        }

        if (request.getGender() == null || request.getGender().trim().isEmpty()) {
            return "Gender is required";
        }

        if (request.getMobile_code() == null || request.getMobile_code().trim().isEmpty()) {
            return "Mobile code is required";
        }

        if (request.getMobile_number() == null || request.getMobile_number().trim().isEmpty()) {
            return "Mobile number is required";
        }

        if (!request.getMobile_number().matches("\\d{10}")) {
            return "Mobile number must be exactly 10 digits and numeric only";
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return "Password is required";
        }

        if (request.getPassword().length() < 6) {
            return "Password must be at least 6 characters long";
        }

        return null; // All validations passed
    }


    public static String validateUserLogin(LoginRequest request) {

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return "Email is required";
        }

        if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "Email format is invalid";
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return "Password is required";
        }

        return null;
    }
}
