package com.food.khaaonow.controller;

import com.food.khaaonow.dto.restaurantonboard.OnboardingRequestRejection;
import com.food.khaaonow.model.RestaurantOnboardingRequest;
import com.food.khaaonow.service.RestaurantOnboardingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/restaurant/onboard")
public class AdminController {

    private final RestaurantOnboardingService restaurantOnboardingService;

    public AdminController(RestaurantOnboardingService restaurantOnboardingService) {
        this.restaurantOnboardingService = restaurantOnboardingService;
    }

    @GetMapping("/request/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantOnboardingRequest> getRestaurantOnboardingRequest(@PathVariable Long id) {
        return new ResponseEntity<>(restaurantOnboardingService.getRestaurantRequestById(id), HttpStatus.OK);
    }

    @GetMapping("/requests")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RestaurantOnboardingRequest>> getRestaurantOnboardingRequests() {
        return new ResponseEntity<>(restaurantOnboardingService.getRestaurantRequests(), HttpStatus.OK);
    }


    @PostMapping("/request/{id}/approve")
       @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveRestaurantOnboardingRequest(@PathVariable Long id) {
        restaurantOnboardingService.approveRequest(id);
        return ResponseEntity.ok("Approved Successfully");
    }

    @PostMapping("/request/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectRestaurantOnboardingRequest(@PathVariable Long id, @RequestBody OnboardingRequestRejection onboardingRequestRejection) {
        restaurantOnboardingService.rejectRequest(id,onboardingRequestRejection);
        return ResponseEntity.ok("Request Rejected");
    }
}
