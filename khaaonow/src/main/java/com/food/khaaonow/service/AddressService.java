package com.food.khaaonow.service;

import com.food.khaaonow.model.AddressSnapshot;
import com.food.khaaonow.model.address.Address;
import com.food.khaaonow.model.address.Country;
import com.food.khaaonow.repo.AddressRepo;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepo addressRepo;

    public AddressService(AddressRepo addressRepo) {
        this.addressRepo = addressRepo;
    }

    public Address createAddress(Address address) {
        return addressRepo.save(address);
    }

//    public Address createAddressFromSnapshot(AddressSnapshot s, Country country) {
//
//    }
}
