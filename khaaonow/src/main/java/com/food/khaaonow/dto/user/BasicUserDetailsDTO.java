package com.food.khaaonow.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BasicUserDetailsDTO {
    private String fullName;
    private String email;
    private String imageUrl;
    private Boolean isVerified;
}
