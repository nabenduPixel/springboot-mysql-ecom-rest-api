package com.example.demo.service;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.demo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {


    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Your secret key
    private long expiration = 86400000; // 24 hour expiration time (in milliseconds)

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", user.getRole()) // Add roles or other claims as needed
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set expiration to 24 hours
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }

    // Method to extract username from token
    public String getEmailFromToken(String token) {
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Email is stored as subject
        } catch (Exception e) {
            return null; // Return null in case of an invalid or expired token
        }
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token); // If parsing is successful, the token is valid
            return true;
        } catch (Exception e) {
            return false; // Token is invalid or expired
        }
    }

}
