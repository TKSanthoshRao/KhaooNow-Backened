package com.food.khaaonow.service;

import com.food.khaaonow.dto.restaurantonboard.OnboardingRequestRejection;
import com.food.khaaonow.dto.restaurantonboard.RestaurantRequest;
import com.food.khaaonow.model.AddressSnapshot;
import com.food.khaaonow.model.RestaurantOnboardingRequest;
import com.food.khaaonow.model.RestaurantOnboardingStatus;
import com.food.khaaonow.model.address.Address;
import com.food.khaaonow.model.address.Country;
import com.food.khaaonow.model.address.RestaurantAddress;
import com.food.khaaonow.model.auth.Role;
import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.model.restaurant.RestaurantStatus;
import com.food.khaaonow.model.user.User;
import com.food.khaaonow.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class RestaurantOnboardingService {

    private AuthService authService;
    private EmailVerificationTokenRepo emailVerificationTokenRepo;
    private CountryService countryService;
    private AddressService addressService;
    private RestaurantAddressRepo  restaurantAddressRepo;
    private RestaurantRepo restaurantRepo;
    private final RestaurantOnboardingRepo restaurantOnboardingRepo;
    private final RolesRepo rolesRepo;
    private final UserService userService;

    public RestaurantOnboardingService(AuthService authService,
                                       EmailVerificationTokenRepo emailVerificationTokenRepo,
                                       CountryService countryService,
                                       AddressService addressService,
                                       RestaurantAddressRepo  restaurantAddressRepo,
                                       RestaurantRepo  restaurantRepo,
                                       RestaurantOnboardingRepo restaurantOnboardingRepo,
                                       RolesRepo rolesRepo,
                                       UserService userService) {
        this.authService = authService;
        this.emailVerificationTokenRepo = emailVerificationTokenRepo;
        this.countryService = countryService;
        this.addressService = addressService;
        this.restaurantAddressRepo = restaurantAddressRepo;
        this.restaurantRepo = restaurantRepo;
        this.restaurantOnboardingRepo = restaurantOnboardingRepo;
        this.rolesRepo = rolesRepo;
        this.userService = userService;
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

        return restaurantOnboardingRepo.findByrestaurantOnboardingStatus(RestaurantOnboardingStatus.REQUESTED);
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

            User loggeInUser = req.getOwner();
            Set<Role> roles = new HashSet<>();
            roles.add(rolesRepo.findByName("ROLE_RESTAURANT_ADMIN"));
            loggeInUser.setRoles(roles);
            User owner = userService.save(loggeInUser);

            Restaurant restaurant = new Restaurant();
            restaurant.setActive(true);
            restaurant.setName(req.getRestaurantName());
            restaurant.setAddress(restaurantAddress1);
            restaurant.setActive(true);
            restaurant.setClosingTime(req.getClosingTime());
            restaurant.setOpeningTime(req.getOpeningTime());
            restaurant.setStatus(RestaurantStatus.OPEN);
            restaurant.setOwner(owner);



            restaurantRepo.save(restaurant);

        req.setRestaurantOnboardingStatus(RestaurantOnboardingStatus.APPROVED);
        req.setReviewer(authService.getCurrentUser());
        req.setReviewedAt(LocalDateTime.now());
        restaurantOnboardingRepo.save(req);
    }


    public void rejectRequest(Long id, OnboardingRequestRejection onboardingRequestRejection) {
        RestaurantOnboardingRequest req = restaurantOnboardingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (req.getRestaurantOnboardingStatus() != RestaurantOnboardingStatus.REQUESTED) {
            throw new IllegalStateException("Invalid state");
        }
        String reason = onboardingRequestRejection.getReason();
        if(reason == null || reason.trim().isEmpty()){
            throw new IllegalStateException("Reason must required for rejection");
        }

        req.setRestaurantOnboardingStatus(RestaurantOnboardingStatus.REJECTED);
        req.setReviewer(authService.getCurrentUser());
        req.setReviewedAt(LocalDateTime.now());
        req.setRejectionReason(onboardingRequestRejection.getReason());
        restaurantOnboardingRepo.save(req);
    }

    public RestaurantOnboardingRequest getRestaurantRequestById(Long id) {
        return restaurantOnboardingRepo.findById(id).get();
    }
}
