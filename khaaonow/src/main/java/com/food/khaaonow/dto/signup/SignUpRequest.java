package com.food.khaaonow.dto.signup;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Name must not be empty")
    private String name;
    @NotBlank(message = "Password must not be empty")
    private String password;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be empty")
    private String email;
}
