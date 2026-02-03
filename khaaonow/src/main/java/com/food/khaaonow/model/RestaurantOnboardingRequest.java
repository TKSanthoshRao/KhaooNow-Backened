package com.food.khaaonow.model;

import com.food.khaaonow.model.address.Address;
import com.food.khaaonow.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "restaurant_onboarding_requests",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "restaurant_name"})
        }
)
@Getter
@Setter
public class RestaurantOnboardingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String restaurantName;

    @Embedded
    private AddressSnapshot address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantOnboardingStatus restaurantOnboardingStatus;


    @Column(nullable = false)
    private LocalTime OpeningTime;

    @Column(nullable = false)
    private LocalTime ClosingTime;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.restaurantOnboardingStatus = RestaurantOnboardingStatus.REQUESTED;
    }
}
