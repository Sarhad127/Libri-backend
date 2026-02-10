package org.tutorial.venusbackend.service;

import org.springframework.stereotype.Service;
import org.tutorial.venusbackend.model.Cart;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.repository.CartRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getOrCreateCart(MyUser user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
}