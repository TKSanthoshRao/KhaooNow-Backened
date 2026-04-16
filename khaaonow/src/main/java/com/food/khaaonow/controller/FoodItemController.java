package com.food.khaaonow.controller;


import com.food.khaaonow.dto.restaurant.FoodItemDTO;
import com.food.khaaonow.model.restaurant.FoodItem;
import com.food.khaaonow.service.FoodItemService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/restaurant")
//@PreAuthorize("hasRole('RESTAURANT_ADMIN')")
public class FoodItemController {
    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @PostMapping("/{restaurantid}/food-items")
    public FoodItem addFoodItem(@RequestBody FoodItemDTO foodItemDTO, @PathVariable Long restaurantid) {
        return foodItemService.addFoodItemToRestaurant(foodItemDTO,restaurantid);
    }
}
