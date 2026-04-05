package com.food.khaaonow.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private BigDecimal distance;
    private Boolean open;
    private String state;
    private String street;
    private String city;
}
