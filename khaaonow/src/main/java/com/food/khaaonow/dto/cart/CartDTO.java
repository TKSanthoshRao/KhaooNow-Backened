package com.food.khaaonow.dto.cart;

import com.food.khaaonow.model.cart.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CartDTO {

    private BigDecimal cartPrice;
    private Set<CartItemsDTO> cartItems;
    private Long cartLength;
    private Long restaurantId;

    public CartDTO(){
        cartPrice = BigDecimal.ZERO;
        cartItems = new HashSet<>();
    }

    public void addCartItem(CartItemsDTO cartItemsDTO) {
        this.cartItems.add(cartItemsDTO);
    }

}
