package com.food.khaaonow.service;


import com.food.khaaonow.controller.CartItemController;
import com.food.khaaonow.dto.cart.CartDTO;
import com.food.khaaonow.exception.CartConflictException;
import com.food.khaaonow.model.cart.Cart;
import com.food.khaaonow.model.cart.CartItem;
import com.food.khaaonow.model.restaurant.FoodItem;
import com.food.khaaonow.repo.CartItemRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartItemService {
    private CartItemRepo cartItemRepo;
    private CartService cartService;
    private FoodItemService foodItemService;
    private static final Logger logger = LoggerFactory.getLogger(CartItemService.class);
    public CartItemService(CartItemRepo cartItemRepo,
                           CartService cartService,
                            FoodItemService foodItemService) {
        this.cartItemRepo = cartItemRepo;
        this.cartService = cartService;
        this.foodItemService = foodItemService;
    }

    @Transactional
    public CartDTO addCartItemToCart(Long foodItemId, Integer quantity,Boolean force) {
        logger.info("Cart addition service started");
        Cart cart = cartService.findLoggedInUserCart();
        FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);

        if(quantity == null || quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if(cart.getRestaurant() != null && !foodItem.getRestaurant().getId().equals(cart.getRestaurant().getId())) {
            if(!force) {
             throw new CartConflictException(
                        "Cart contains items from another restaurant",
                        cart.getRestaurant().getId()
                );
            }
                cart.getCartItems().clear();
                cart.setRestaurant(foodItem.getRestaurant());
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream().filter(item -> item.getFoodItem().getId().equals(foodItemId))
                                            .findFirst();
        if(existingItem.isPresent()){
            CartItem existingCartItem = existingItem.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setFoodItem(foodItem);
            cartItem.setQuantity(quantity);
            cart.addCartItem(cartItem);

        }
        if (cart.getRestaurant() == null) {
            cart.setRestaurant(foodItem.getRestaurant());
        }
        return cartService.getCartDTO(cartService.saveCart(cart));

    }
}
