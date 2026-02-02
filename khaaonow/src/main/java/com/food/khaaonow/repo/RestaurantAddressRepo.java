package com.food.khaaonow.repo;

import com.food.khaaonow.model.address.RestaurantAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantAddressRepo extends JpaRepository<RestaurantAddress, Integer> {
}
