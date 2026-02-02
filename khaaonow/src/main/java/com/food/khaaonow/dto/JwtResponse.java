package com.food.khaaonow.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private long expiresIn;
}
