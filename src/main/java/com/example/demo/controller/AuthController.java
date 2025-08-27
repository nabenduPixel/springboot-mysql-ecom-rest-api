package com.example.demo.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserRegistrationRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ResponseHandeler;
import com.example.demo.validation.AuthValidation;
import com.example.demo.service.JwtTokenProvider;
import com.example.demo.service.TokenBlacklistService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            String validationError = AuthValidation.validateUserRegistration(request);
            if (validationError != null) {
                return ResponseHandeler.generateResponse(
                        HttpStatus.BAD_REQUEST,
                        validationError,
                        null);
            }
            if (userService.findByemail(request.getEmail()).isPresent()) {
                return ResponseHandeler.generateResponse(
                        HttpStatus.BAD_REQUEST,
                        "Email is already in use",
                        null);
            }

            if (userRepository.findByMobileNumber(request.getMobile_number()).isPresent()) {
                return ResponseHandeler.generateResponse(
                        HttpStatus.BAD_REQUEST,
                        "Mobile number is already in use",
                        null);
            }
            userService.registerUser(request);

            return ResponseHandeler.generateResponse(
                    HttpStatus.CREATED,
                    "Registration successfully completed",
                    request);
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred: " + e.getMessage(),
                    null);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String validationError = AuthValidation.validateUserLogin(request);
            if (validationError != null) {
                return ResponseHandeler.generateResponse(
                        HttpStatus.BAD_REQUEST,
                        validationError,
                        null);
            }

            Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
            if (optionalUser.isEmpty()) {
                return ResponseHandeler.generateResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password.", null);
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseHandeler.generateResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password.", null);
            }

            LoginResponse response = userService.login(request);

            return ResponseHandeler.generateResponse(
                    HttpStatus.OK,
                    "Login successful",
                    response);
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred: " + e.getMessage(),
                    null);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                tokenBlacklistService.blacklistToken(token);
                return ResponseHandeler.generateResponse(HttpStatus.OK,
                        "Logout successful. Token has been invalidated.", null);
            } else {
                return ResponseHandeler.generateResponse(HttpStatus.BAD_REQUEST,
                        "Missing or invalid Authorization header.", null);
            }
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error during logout: " + e.getMessage(), null);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtTokenProvider.getEmailFromToken(token);
            Optional<User> user = userService.findByemail(email);

            if (user == null) {
                return ResponseHandeler.generateResponse( HttpStatus.UNAUTHORIZED,"User not found.", null);
            }

            return ResponseHandeler.generateResponse(HttpStatus.OK,"User profile retrieved successfully.", user);
        } catch (Exception e) {
            return ResponseHandeler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid or expired token.",  null);
        }
    }

}
       
