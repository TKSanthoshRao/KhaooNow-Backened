package com.food.khaaonow.service;

import com.food.khaaonow.dto.*;
import com.food.khaaonow.model.otp.EmailVerificationToken;
import com.food.khaaonow.model.user.User;
import com.food.khaaonow.repo.EmailVerificationTokenRepo;
import com.food.khaaonow.repo.RolesRepo;
import com.food.khaaonow.repo.UserRepo;
import jakarta.annotation.Nullable;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RolesRepo rolesRepo;
    private final JwtService jwtService;
    private final VerificationService verificationService;

    public AuthService(UserService userService,AuthenticationManager authenticationManager,BCryptPasswordEncoder bCryptPasswordEncoder,RolesRepo rolesRepo,JwtService jwtService,VerificationService verificationService) {
        this.userService=userService;
        this.authenticationManager=authenticationManager;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.rolesRepo=rolesRepo;
        this.jwtService=jwtService;
        this.verificationService=verificationService;
    }

    public void registerUser(SignUpRequest signUpUser) {
        if(!verificationService.isUserEmailVerified(signUpUser.getEmail())) {
            throw new RuntimeException("Email not verified please first verify your email");
        } else if (userService.isUserAlreadyExists(signUpUser.getEmail())) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
                user.setFullName(signUpUser.getName());
                user.setPassword(bCryptPasswordEncoder.encode(signUpUser.getPassword()));
                user.setEmail(signUpUser.getEmail());
                user.setRoles(Set.of(rolesRepo.findByName("ROLE_USER")));
                user.setIsEmailVerified(true);
                User savedUser = userService.save(user);
    }

    public JwtResponse loginUser(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails userDetails){
            String jwtToken = jwtService.generateToken(userDetails);
            return new JwtResponse(jwtToken,"Bearer",180L);
        }
        return null;
    }

    public void logoutUser() {
    }

    public @Nullable User getCurrentUser() {
        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = null;
        if (loggedInUserEmail != null){
            user = userService.findUserByEmail(loggedInUserEmail);
        }
        return user;
    }

}
