package com.food.khaaonow.service;

import com.food.khaaonow.repo.UserRepo;
import com.food.khaaonow.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findUserByEmail(String email) throws IllegalArgumentException {
        if(email == null || email.isEmpty()) throw new IllegalArgumentException("email required");
       return userRepo.findByEmail(email);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public boolean isUserAlreadyExists(String email) {
        return userRepo.findByEmail(email) != null;
    }

    public void markEmailVerified(User user) {
        user.setIsEmailVerified(true);
    }
}
