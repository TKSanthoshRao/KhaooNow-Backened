package com.food.khaaonow.repo;

import com.food.khaaonow.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.food.khaaonow.model.cart.CartStatus;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserIdAndCartStatus(Long userId, CartStatus cartStatus);
}
