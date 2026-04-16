package com.food.khaaonow.exception;

import lombok.Getter;

@Getter
public class CartConflictException extends RuntimeException{
    private Long existingRestaurantId;

    public CartConflictException(String message , Long existingRestaurantId) {
        super(message);
        this.existingRestaurantId = existingRestaurantId;
    }
}
