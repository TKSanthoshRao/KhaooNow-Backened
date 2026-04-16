package com.food.khaaonow.service;

import com.food.khaaonow.dto.cart.CartDTO;
import com.food.khaaonow.dto.cart.CartItemsDTO;
import com.food.khaaonow.dto.restaurant.FoodItemDTO;
import com.food.khaaonow.model.cart.Cart;
import com.food.khaaonow.model.cart.CartItem;
import com.food.khaaonow.model.cart.CartStatus;
import com.food.khaaonow.model.user.User;
import com.food.khaaonow.repo.CartRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CartService {

    private AuthService authService;
    private CartRepo cartRepo;
    public CartService(AuthService authService, CartRepo cartRepo) {
        this.authService = authService;
        this.cartRepo = cartRepo;
    }
    public Cart findLoggedInUserCart() {
        User user = authService.getCurrentUser();
        return cartRepo.findByUserIdAndCartStatus(user.getId(), CartStatus.ACTIVE).orElseGet(this::createNewCartForCurrentUser);
    }

    public Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

    @Transactional(readOnly = true)
    public CartDTO getCartDTO(Cart cart) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        CartDTO cartDTO = new CartDTO(new BigDecimal(0), new HashSet<>(), 0L,-1L);

        if (cart == null) return cartDTO;

        Long cartTotalItems = 0L;

        for (CartItem c : cart.getCartItems()) {
            FoodItemDTO foodItemDTO = new FoodItemDTO(
                    c.getFoodItem().getName(),
                    c.getFoodItem().getPrice(),
                    c.getFoodItem().getFoodType()
            );

            CartItemsDTO cartItemsDTO = new CartItemsDTO();
            cartItemsDTO.addfoodItem(foodItemDTO);
            cartItemsDTO.setQuantity(c.getQuantity());

            cartDTO.addCartItem(cartItemsDTO);

            BigDecimal itemPrice = c.getFoodItem().getPrice()
                    .multiply(BigDecimal.valueOf(c.getQuantity()));

            totalPrice = totalPrice.add(itemPrice);
            cartTotalItems += c.getQuantity();
        }

        cartDTO.setCartLength(cartTotalItems);
        cartDTO.setCartPrice(totalPrice);
        cartDTO.setRestaurantId(cart.getRestaurant().getId());

        return cartDTO;
    }

    public CartDTO getCartDTOForCurrentUser() {
        User user = authService.getCurrentUser();

        Optional<Cart> cart = cartRepo
                .findByUserIdAndCartStatus(user.getId(), CartStatus.ACTIVE);

        return cart.map(this::getCartDTO)
                .orElseGet(() -> new CartDTO(BigDecimal.ZERO, new HashSet<>(), 0L,-1L));
    }

    public void deleteExistingCart(Cart cart) {
        cartRepo.delete(cart);
    }

    public Cart createNewCartForCurrentUser() {
        Cart newCart = new Cart();
        newCart.setUser(authService.getCurrentUser());
        newCart.setCartStatus(CartStatus.ACTIVE);
        return newCart;
    }
}
