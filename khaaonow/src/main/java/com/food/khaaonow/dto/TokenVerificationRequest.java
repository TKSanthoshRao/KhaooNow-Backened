package com.food.khaaonow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenVerificationRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "OTP must not be empty")
    private String token;
}
