package com.food.khaaonow.model.order;
import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Getter
@Setter
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItem = new HashSet<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOrderAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id",nullable = false)
    private OrderAddress deliveryAddress;

    @CreationTimestamp
    @Column(nullable = false, updatable = false,name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false,updatable = false,name = "updated_at")
    private LocalDateTime updatedAt;

}
