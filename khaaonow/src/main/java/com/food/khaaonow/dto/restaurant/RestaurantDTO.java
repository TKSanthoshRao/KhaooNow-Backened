package com.food.khaaonow.dto.restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private Double distance;
    private Boolean open;
    private String state;
    private String street;
    private String city;
    private String RestaurantImage;
}
