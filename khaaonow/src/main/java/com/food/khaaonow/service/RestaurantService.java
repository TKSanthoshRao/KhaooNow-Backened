package com.food.khaaonow.service;

import com.food.khaaonow.dto.restaurant.RestaurantDTO;
import com.food.khaaonow.model.address.Address;

import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.model.restaurant.RestaurantStatus;
import com.food.khaaonow.repo.RestaurantAddressRepo;
import com.food.khaaonow.repo.RestaurantRepo;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class RestaurantService {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private final RestaurantRepo restaurantRepo;
    private final AddressService addressService;
    private final CountryService countryService;
    private final RestaurantAddressRepo  restaurantAddressRepo;
    private final AuthService authService;

    @Autowired
    public RestaurantService(RestaurantRepo restaurantRepo,
                             AddressService addressService,
                             CountryService countryService,
                             RestaurantAddressRepo restaurantAddressRepo,
                             AuthService authService) {
        this.restaurantRepo = restaurantRepo;
        this.addressService = addressService;
        this.countryService = countryService;
        this.restaurantAddressRepo = restaurantAddressRepo;
        this.authService = authService;

    }


    public List<RestaurantDTO> getNearbyRestaurants(BigDecimal usrLat, BigDecimal usrLng, double radius) {


        double lat = usrLat.doubleValue();
        double lng = usrLng.doubleValue();

        double latRange = radius / 111.0;
        double lngRange = radius / (111.0 * Math.cos(Math.toRadians(lat)));

        double minLat = lat - latRange;
        double maxLat = lat + latRange;
        double minLng = lng - lngRange;
        double maxLng = lng + lngRange;

        return restaurantRepo.findNearbyRestaurants(
                usrLat.doubleValue(),
                usrLng.doubleValue(),
                radius,minLat,maxLat,minLng,maxLng
        ).stream().map(r -> new RestaurantDTO(
                r.getId(),r.getName(),r.getDistance()
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue()
                ,true,r.getState(),r.getStreet(),r.getCity(),r.getRestaurantImage()
        )).toList();
    }

    private Boolean isOpen(RestaurantStatus status) {
        return status.equals(RestaurantStatus.OPEN);
    }

    public Restaurant findRestaurantById(Long restaurantid) {
        return restaurantRepo.findById(restaurantid).orElse(null);
    }

}
