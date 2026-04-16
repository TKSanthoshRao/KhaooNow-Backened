package com.food.khaaonow.controller;


import com.food.khaaonow.dto.restaurant.RestaurantDTO;
import com.food.khaaonow.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByLatitudeAndLongitude(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lng,
            @RequestParam(defaultValue = "5") double radius
    ) {
        return ResponseEntity.ok(
                restaurantService.getNearbyRestaurants(lat, lng, radius)
        );
    }
}
