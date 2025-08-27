package com.example.demo.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandeler {

    public static ResponseEntity<Object> generateResponse(HttpStatus status, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("res_code", status.value());
        response.put("message", message);
        response.put("data", data);

        return new ResponseEntity<>(response, status);
    }

}
