package com.food.khaaonow.repo;

import com.food.khaaonow.model.restaurant.FoodItem;
import com.food.khaaonow.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FoodItemRepo extends JpaRepository<FoodItem, Long> {

    @Query("SELECT f FROM FoodItem f WHERE f.restaurant.id = :restaurantId")
    List<FoodItem> getFoodItems(@Param("restaurantId") Long restaurantId);

    List<FoodItem> findByRestaurantIdAndActiveTrue(Long id);

}
