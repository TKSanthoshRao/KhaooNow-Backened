package com.food.khaaonow.service;

import com.food.khaaonow.dto.RestaurantRequest;
import com.food.khaaonow.model.AddressSnapshot;
import com.food.khaaonow.model.RestaurantOnboardingRequest;
import com.food.khaaonow.model.RestaurantOnboardingStatus;
import com.food.khaaonow.model.address.Address;
import com.food.khaaonow.model.address.Country;
import com.food.khaaonow.model.address.RestaurantAddress;
import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.model.restaurant.RestaurantStatus;
import com.food.khaaonow.model.user.User;
import com.food.khaaonow.repo.EmailVerificationTokenRepo;
import com.food.khaaonow.repo.RestaurantAddressRepo;
import com.food.khaaonow.repo.RestaurantOnboardingRepo;
import com.food.khaaonow.repo.RestaurantRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RestaurantOnboardingService {

    private AuthService authService;
    private EmailVerificationTokenRepo emailVerificationTokenRepo;
    private CountryService countryService;
    private AddressService addressService;
    private RestaurantAddressRepo  restaurantAddressRepo;
    private RestaurantRepo restaurantRepo;
    private final RestaurantOnboardingRepo restaurantOnboardingRepo;

    public RestaurantOnboardingService(AuthService authService,EmailVerificationTokenRepo emailVerificationTokenRepo,CountryService countryService,AddressService addressService,RestaurantAddressRepo  restaurantAddressRepo,RestaurantRepo  restaurantRepo,RestaurantOnboardingRepo restaurantOnboardingRepo) {
        this.authService = authService;
        this.emailVerificationTokenRepo = emailVerificationTokenRepo;
        this.countryService = countryService;
        this.addressService = addressService;
        this.restaurantAddressRepo = restaurantAddressRepo;
        this.restaurantRepo = restaurantRepo;
        this.restaurantOnboardingRepo = restaurantOnboardingRepo;
    }

    @Transactional
    public void createRestaurantOnBoardingRequest(RestaurantRequest restaurantRequest) {

        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not logged in");
        }

        if (!emailVerificationTokenRepo.isEmailVerified(currentUser.getEmail())) {
            throw new IllegalStateException("Email not verified");
        }

        if (restaurantOnboardingRepo.existsByOwnerAndRestaurantOnboardingStatus(
                currentUser,
                RestaurantOnboardingStatus.REQUESTED
        )) {
            throw new IllegalStateException("Onboarding request already pending");
        }

        AddressSnapshot snapshot = new AddressSnapshot();
        snapshot.setCountry(restaurantRequest.getCountry());
        snapshot.setState(restaurantRequest.getState());
        snapshot.setCity(restaurantRequest.getCity());
        snapshot.setStreet(restaurantRequest.getStreet());
        snapshot.setZipcode(restaurantRequest.getZipcode());
        snapshot.setLatitude(restaurantRequest.getLatitude());
        snapshot.setLongitude(restaurantRequest.getLongitude());

        RestaurantOnboardingRequest request = new RestaurantOnboardingRequest();
        request.setRestaurantName(restaurantRequest.getRestaurantName());
        request.setAddress(snapshot);
        request.setOwner(currentUser);
        request.setOpeningTime(restaurantRequest.getOpeningTime());
        request.setClosingTime(restaurantRequest.getClosingTime());
        request.setRestaurantOnboardingStatus(RestaurantOnboardingStatus.REQUESTED);

        restaurantOnboardingRepo.save(request);
    }


    public List<RestaurantOnboardingRequest> getRestaurantRequests() {

        return restaurantOnboardingRepo.findAll();
    }

    @Transactional
    public void approveRequest(Long id) {

        RestaurantOnboardingRequest req = restaurantOnboardingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (req.getRestaurantOnboardingStatus() != RestaurantOnboardingStatus.REQUESTED) {
            throw new IllegalStateException("Invalid state");
        }

        AddressSnapshot s = req.getAddress();

            Country country = new Country();
            country.setName(s.getCountry());
            country.setCode(s.getCountry().substring(0,2));
            country.setActive(true);
            Country RestaurantCountry = countryService.saveOrUpdateCountry(country);

            Address address = new Address();
            address.setLatitude(s.getLatitude());
            address.setLongitude(s.getLongitude());
            address.setCity(s.getCity());
            address.setState(s.getState());
            address.setActive(true);
            address.setZipcode(s.getZipcode());
            address.setStreet(s.getStreet());
            address.setCountry(RestaurantCountry);
            Address address1 = addressService.createAddress(address);

            RestaurantAddress restaurantAddress = new RestaurantAddress();
            restaurantAddress.setAddress(address1);
            RestaurantAddress restaurantAddress1 = restaurantAddressRepo.save(restaurantAddress);

            Restaurant restaurant = new Restaurant();
            restaurant.setActive(true);
            restaurant.setName(req.getRestaurantName());
            restaurant.setAddress(restaurantAddress1);
            restaurant.setActive(true);
            restaurant.setClosingTime(req.getClosingTime());
            restaurant.setOpeningTime(req.getOpeningTime());
            restaurant.setStatus(RestaurantStatus.OPEN);
            restaurant.setOwner(req.getOwner());

            restaurantRepo.save(restaurant);

        req.setRestaurantOnboardingStatus(RestaurantOnboardingStatus.APPROVED);
        restaurantOnboardingRepo.save(req);
    }


}
