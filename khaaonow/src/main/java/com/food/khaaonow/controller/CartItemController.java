package com.food.khaaonow.controller;

import com.food.khaaonow.dto.cart.CartDTO;
import com.food.khaaonow.service.CartItemService;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Validated
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/items/{foodItemId}")
    public ResponseEntity<CartDTO> addCartItemToCart(
            @PathVariable Long foodItemId,
            @RequestParam @Min(1) Integer quantity,
            @RequestParam(defaultValue = "false") boolean force) {
        CartDTO cartDTO = cartItemService.addCartItemToCart(foodItemId, quantity, force);

        return ResponseEntity.ok(cartDTO);
    }

}
