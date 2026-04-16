package com.food.khaaonow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CartConflictException.class)
    public ResponseEntity<?> handleException(CartConflictException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error","CART_CONFLICT");
        response.put("message", ex.getMessage());
        response.put("existingRestaurantId", ex.getExistingRestaurantId());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

    }
}
