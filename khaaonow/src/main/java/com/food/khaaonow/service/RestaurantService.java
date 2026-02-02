package com.food.khaaonow.service;

import com.food.khaaonow.dto.RestaurantDTO;
import com.food.khaaonow.dto.RestaurantRequest;
import com.food.khaaonow.model.address.Address;
import com.food.khaaonow.model.address.Country;
import com.food.khaaonow.model.address.RestaurantAddress;
import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.model.restaurant.RestaurantStatus;
import com.food.khaaonow.repo.RestaurantAddressRepo;
import com.food.khaaonow.repo.RestaurantRepo;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class RestaurantService {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private final RestaurantRepo restaurantRepo;
    private final AddressService addressService;
    private final CountryService countryService;
    private final RestaurantAddressRepo  restaurantAddressRepo;

    @Autowired
    public RestaurantService(RestaurantRepo restaurantRepo, AddressService addressService, CountryService countryService, RestaurantAddressRepo restaurantAddressRepo) {
        this.restaurantRepo = restaurantRepo;
        this.addressService = addressService;
        this.countryService = countryService;
        this.restaurantAddressRepo = restaurantAddressRepo;
    }


    public @Nullable List<RestaurantDTO> getNearbyRestaurants(BigDecimal usrLat, BigDecimal usrLng, double radius) {

        List<Restaurant> allRestaurants = restaurantRepo.findAll();

        return allRestaurants.stream().map(restaurant -> {
            Address address = restaurant.getAddress().getAddress();

            BigDecimal distance = calculateDistance(usrLat,usrLng,address.getLatitude(),address.getLongitude());

            if(distance.doubleValue()<= radius){
                return new RestaurantDTO(restaurant.getId(),restaurant.getName(),distance,isOpen(restaurant.getStatus()));
            }

            return null;
        }).filter(Objects::nonNull)
                .sorted(Comparator.comparing(RestaurantDTO::getDistance))
                .toList();
    }

    private Boolean isOpen(RestaurantStatus status) {
        return status.equals(RestaurantStatus.OPEN);
    }


    private BigDecimal calculateDistance(
            BigDecimal lat1,
            BigDecimal lon1,
            BigDecimal lat2,
            BigDecimal lon2
    ) {
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue()))
                * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return BigDecimal.valueOf(distance)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    public String createRestaurant(@Valid RestaurantRequest restaurantRequest) {
        Country country = new Country();
        country.setName(restaurantRequest.getCountry());
        country.setCode(restaurantRequest.getCountry().substring(0,3));
        country.setActive(true);
        Country RestaurantCountry = countryService.saveOrUpdateCountry(country);

        Address address = new Address();
        address.setLatitude(restaurantRequest.getLatitude());
        address.setLongitude(restaurantRequest.getLongitude());
        address.setCity(restaurantRequest.getCity());
        address.setState(restaurantRequest.getState());
        address.setActive(true);
        address.setZipcode(restaurantRequest.getZipcode());
        address.setCountry(RestaurantCountry);
        Address address1 = addressService.createAddress(address);

        RestaurantAddress restaurantAddress = new RestaurantAddress();
        restaurantAddress.setAddress(address1);
        RestaurantAddress restaurantAddress1 = restaurantAddressRepo.save(restaurantAddress);

        Restaurant restaurant = new Restaurant();
        restaurant.setActive(true);
        restaurant.setName(restaurantRequest.getRestaurantName());
        restaurant.setAddress(restaurantAddress1);
        restaurant.setActive(true);
        restaurant.setClosingTime(restaurantRequest.getClosingTime());
        restaurant.setOpeningTime(restaurantRequest.getOpeningTime());
        restaurant.setStatus(RestaurantStatus.OPEN);

        restaurantRepo.save(restaurant);
        return "success";

    }
}
