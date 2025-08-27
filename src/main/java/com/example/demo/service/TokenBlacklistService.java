package com.example.demo.service;

import java.security.Key;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());
    private final Key secretKey;

    public TokenBlacklistService(Key secretKey) {
        this.secretKey = secretKey;
    }

    public void blacklistToken(String token) {
        try {
            long expiration = extractExpirationFromToken(token);
            long ttl = expiration - System.currentTimeMillis();

            blacklist.add(token);

            // Schedule removal after expiration
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    blacklist.remove(token);
                }
            }, ttl > 0 ? ttl : 3600000); // fallback to 1 hour

        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token: " + e.getMessage());
        }
    }

    private long extractExpirationFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime();
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

}
