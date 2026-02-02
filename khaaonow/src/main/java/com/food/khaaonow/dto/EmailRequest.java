package com.food.khaaonow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
public class EmailRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be empty")
    private String email;
}
