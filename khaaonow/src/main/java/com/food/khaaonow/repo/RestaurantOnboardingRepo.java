package com.food.khaaonow.repo;

import com.food.khaaonow.model.RestaurantOnboardingRequest;
import com.food.khaaonow.model.RestaurantOnboardingStatus;
import com.food.khaaonow.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantOnboardingRepo extends JpaRepository<RestaurantOnboardingRequest, Long> {
    //boolean existsByOwnerAndStatus(User currentUser, RestaurantOnboardingStatus restaurantOnboardingStatus);

    boolean existsByOwnerAndRestaurantOnboardingStatus(User currentUser, RestaurantOnboardingStatus restaurantOnboardingStatus);
}
