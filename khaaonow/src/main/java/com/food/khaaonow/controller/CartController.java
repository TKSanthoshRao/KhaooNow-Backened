package com.food.khaaonow.controller;

import com.food.khaaonow.dto.cart.CartDTO;
import com.food.khaaonow.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {


    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/items")
    public ResponseEntity<CartDTO> getCart(){
        return new ResponseEntity<>(cartService.getCartDTOForCurrentUser(), HttpStatus.OK);
    }
}
