package com.food.khaaonow.dto.jwt;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private long expiresIn;
}
