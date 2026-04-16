package com.food.khaaonow.controller;

import com.food.khaaonow.dto.email.EmailRequest;
import com.food.khaaonow.dto.otp.EmailTokenStatus;
import com.food.khaaonow.dto.jwt.JwtResponse;
import com.food.khaaonow.dto.login.LoginRequest;
import com.food.khaaonow.dto.otp.TokenVerificationRequest;
import com.food.khaaonow.dto.otp.VerificationStatus;
import com.food.khaaonow.dto.signup.SignUpRequest;
import com.food.khaaonow.dto.user.BasicUserDetailsDTO;
import com.food.khaaonow.service.AuthService;
import com.food.khaaonow.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final VerificationService verificationService;

    public AuthController(AuthService authService, VerificationService verificationService) {
        this.authService = authService;
        this.verificationService = verificationService;
    }

    @PostMapping("/email-verification/token")
    public ResponseEntity<EmailTokenStatus> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        return new ResponseEntity<>(verificationService.sendVerificationEmailToUser(emailRequest),HttpStatus.OK);
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<VerificationStatus> verifyOTP(@Valid @RequestBody TokenVerificationRequest tokenVerificationRequest) {
        return new ResponseEntity<>(verificationService.verifyEmail(tokenVerificationRequest),HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request){
                authService.registerUser(request);
                return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request){
        return new ResponseEntity<>(authService.loginUser(request), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(){
        authService.logoutUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/loggedin/user")
    public ResponseEntity<BasicUserDetailsDTO> loggedinUser(){
        return new ResponseEntity<>(authService.getLoggedInUserUser(),HttpStatus.OK);
    }
}
