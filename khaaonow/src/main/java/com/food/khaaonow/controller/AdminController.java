package com.food.khaaonow.controller;

import com.food.khaaonow.model.RestaurantOnboardingRequest;
import com.food.khaaonow.model.RestaurantOnboardingStatus;
import com.food.khaaonow.service.RestaurantOnboardingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole(ADMIN)")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final RestaurantOnboardingService restaurantOnboardingService;

    public AdminController(RestaurantOnboardingService restaurantOnboardingService) {
        this.restaurantOnboardingService = restaurantOnboardingService;
    }

    @GetMapping("restaurant/onboard-requests")
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<List<RestaurantOnboardingRequest>> getRestaurantOnboardingRequests() {
        return new ResponseEntity<>(restaurantOnboardingService.getRestaurantRequests(), HttpStatus.OK);
    }


    @PostMapping("restaurant/onboard-request/{id}/action")
//    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<?> ActionRestaurantOnboardingRequest(@PathVariable Long id) {
       restaurantOnboardingService.approveRequest(id);
        return ResponseEntity.ok("Approved Successfully");
    }
}
