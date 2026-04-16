package com.food.khaaonow.service;

import java.math.BigDecimal;

public interface RestaurantNearbyDTO {
    Long getId();
    String getName();
    String getStatus();
    String getRestaurantImage();
    String getState();
    String getStreet();
    String getCity();
    BigDecimal getDistance();
}