package com.food.khaaonow.service;

import com.food.khaaonow.dto.jwt.JwtResponse;
import com.food.khaaonow.dto.login.LoginRequest;
import com.food.khaaonow.dto.signup.SignUpRequest;
import com.food.khaaonow.dto.user.BasicUserDetailsDTO;
import com.food.khaaonow.model.user.User;
import com.food.khaaonow.repo.RolesRepo;
import jakarta.annotation.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

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
            return new JwtResponse(jwtToken,"Bearer",10L);
        }
        return null;
    }

    public void logoutUser() {
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        UserPrinciple userPrinciple =
                (UserPrinciple) authentication.getPrincipal();
        assert userPrinciple != null;
        return userPrinciple.getUser();
    }

    public BasicUserDetailsDTO getLoggedInUserUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        UserPrinciple userPrinciple =
                (UserPrinciple) authentication.getPrincipal();
        assert userPrinciple != null;
        User user =  userPrinciple.getUser();
        return new BasicUserDetailsDTO(user.getFullName(),user.getEmail(),"https://photos.airmail.news/jxwdcgbj07pdyi1tekti725l32s4-4ac75b603d1c17e5a9afaa413ba0920e.jpg",user.getIsEmailVerified());
    }

}
