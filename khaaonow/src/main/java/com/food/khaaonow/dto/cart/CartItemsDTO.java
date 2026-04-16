package com.food.khaaonow.dto.cart;
import com.food.khaaonow.dto.restaurant.FoodItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CartItemsDTO {

    private Set<FoodItemDTO> foodItemDto;
    private Integer quantity;

    public CartItemsDTO() {
        quantity = 0;
        foodItemDto = new HashSet<>();
    }

    public void addfoodItem(FoodItemDTO foodItemDto){
        this.foodItemDto.add(foodItemDto);
    }

}
