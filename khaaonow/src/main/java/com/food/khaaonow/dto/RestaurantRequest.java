package com.food.khaaonow.dto;

import com.food.khaaonow.model.address.Country;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter

public class RestaurantRequest {

    @NotBlank
    private String restaurantName;

    @NotBlank
    private String country;

    @NotBlank
    private String state;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String zipcode;

    @NotBlank
    private BigDecimal latitude;

    @NotBlank
    private BigDecimal longitude;

    @NotBlank
    private LocalTime openingTime;

    @NotBlank
    private LocalTime closingTime;

    @NotBlank
    private String ownerName;

    @Email
    @NotBlank
    private String ownerEmail;

}
