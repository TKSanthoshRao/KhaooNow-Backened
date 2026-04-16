package com.food.khaaonow.controller;

import com.food.khaaonow.service.RestaurantOnboardingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.food.khaaonow.dto.restaurantonboard.RestaurantRequest;

@RestController
@RequestMapping("/api/v1/restaurant-onboarding")
public class RestaurantOnboardingController {

    private final RestaurantOnboardingService onboardingService;

    public RestaurantOnboardingController(RestaurantOnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestOnboarding(@Valid @RequestBody RestaurantRequest restaurantRequest) {
            onboardingService.createRestaurantOnBoardingRequest(restaurantRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Restaurant onboarding request submitted");
    }

}
