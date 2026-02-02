package com.food.khaaonow.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.food.khaaonow.model.address.Address;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address,Long> {

}
