package com.food.khaaonow.service;

import com.food.khaaonow.model.address.Country;
import com.food.khaaonow.repo.CountryRepo;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepo countryRepo;
    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    public Country saveOrUpdateCountry(Country country) {
        return countryRepo.save(country);
    }
}
