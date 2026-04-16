package com.food.khaaonow.service;

import com.food.khaaonow.dto.restaurant.FoodItemDTO;
import com.food.khaaonow.model.restaurant.FoodItem;
import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.repo.FoodItemRepo;
import com.food.khaaonow.repo.RolesRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    private final FoodItemRepo foodItemRepo;
    private final AuthService authService;
    private final RestaurantService restaurantService;
    private final RolesRepo rolesRepo;
    public FoodItemService(FoodItemRepo foodItemRepo,
                           AuthService authService,
                           RestaurantService restaurantService,
                            RolesRepo rolesRepo) {
//        this.restaurantService = restaurantService;
        this.foodItemRepo = foodItemRepo;
        this.authService = authService;
        this.restaurantService = restaurantService;
        this.rolesRepo = rolesRepo;
    }
    public List<FoodItem> getRestaurantFoodItems(Long id) {
       return foodItemRepo.findByRestaurantIdAndActiveTrue(id);
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isRestaurantOwner(#restaurantid, authentication.principal.id)")
    public FoodItem addFoodItemToRestaurant(FoodItemDTO foodItemDTO, Long restaurantid) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantid);
        if(restaurant == null){
            throw new IllegalArgumentException("Not found restaurant with id: " + restaurantid);
        }
            FoodItem foodItem = new FoodItem();
            foodItem.setRestaurant(restaurant);
            foodItem.setActive(true);
            foodItem.setName(foodItemDTO.getItemName());
            foodItem.setAvailable(true);
            foodItem.setPrice(foodItemDTO.getItemPrice());
            foodItem.setFoodType(foodItemDTO.getFoodType());
            return foodItemRepo.save(foodItem);
    }

    public FoodItem getFoodItemById(Long foodItemId) {
        return foodItemRepo.findById(foodItemId).get();
    }
}
