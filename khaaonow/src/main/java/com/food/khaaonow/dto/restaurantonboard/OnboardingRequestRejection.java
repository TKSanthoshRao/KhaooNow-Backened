package com.food.khaaonow.dto.restaurantonboard;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OnboardingRequestRejection {
    private Long id;
    @NotBlank(message = "Rejection reason is required")
    private String reason;
}
