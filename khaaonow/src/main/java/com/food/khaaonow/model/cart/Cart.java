package com.food.khaaonow.model.cart;


import com.food.khaaonow.model.order.OrderStatus;
import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.model.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false,name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus cartStatus;

    @PrePersist
    public void prePersist() {
        if(this.cartStatus == null) {
            this.cartStatus = CartStatus.ACTIVE;
        }
    }

}
