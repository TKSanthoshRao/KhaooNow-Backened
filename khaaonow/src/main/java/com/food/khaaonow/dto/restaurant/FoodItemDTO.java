package com.food.khaaonow.dto.restaurant;

import com.food.khaaonow.model.restaurant.FoodType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class FoodItemDTO {

    @NotNull(message = "Food item name cannot be empty")
    private String itemName;

    @NotNull(message = "Food item price cannot be empty")
    private BigDecimal itemPrice;

    @NotNull(message = "Food type must be VEG, NON_VEG, or VEGAN")
    private FoodType foodType;
}