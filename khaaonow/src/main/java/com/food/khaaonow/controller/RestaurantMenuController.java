package com.food.khaaonow.controller;

import com.food.khaaonow.model.restaurant.FoodItem;
import com.food.khaaonow.service.FoodItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantMenuController {

    private FoodItemService foodItemService;

    public RestaurantMenuController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping("{id}/menu")
    public List<FoodItem> getRestaurantMenu(@PathVariable Long id) {
        return foodItemService.getRestaurantFoodItems(id);
    }
}
