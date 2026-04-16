package com.food.khaaonow.service;

import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private RestaurantService restaurantService;

    public boolean isRestaurantOwner(Long restaurantId, Long userId) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        return restaurant.getOwner().getId().equals(userId);
    }
}