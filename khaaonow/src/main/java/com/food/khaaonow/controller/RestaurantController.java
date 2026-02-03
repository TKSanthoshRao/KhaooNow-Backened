package com.food.khaaonow.controller;


import com.food.khaaonow.dto.RestaurantDTO;
import com.food.khaaonow.dto.RestaurantRequest;
import com.food.khaaonow.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
            @RequestParam(defaultValue = "6371") double radius
    ) {
        return ResponseEntity.ok(
                restaurantService.getNearbyRestaurants(lat, lng, radius)
        );
    }




}
